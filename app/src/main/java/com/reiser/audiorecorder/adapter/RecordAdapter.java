package com.reiser.audiorecorder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.reiser.audiorecorder.R;
import com.reiser.audiorecorder.bean.RecordEntity;
import com.reiser.audiorecorder.helper.AudioPlayHelper;
import com.reiser.audiorecorder.helper.DBHelper;
import com.reiser.audiorecorder.listeners.AudioPlayChangedListener;
import com.reiser.audiorecorder.listeners.SlidingMenuChangeListener;
import com.reiser.audiorecorder.widgets.SlidingMenu;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by reiserx on 2018/12/10.
 * desc : 录音适配器
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private List<RecordEntity> mDatas;
    private DBHelper mDbHelper;
    private AudioPlayHelper mAudioPlayHelper = new AudioPlayHelper();
    private DateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private int mOpenMenuIndex = -1;
    private SlidingMenu mSmOpen;

    public RecordAdapter(Context context) {
        super();
        mDbHelper = new DBHelper(context);
        this.mDatas = mDbHelper.getRecords();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_list_record, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        RecordEntity item = getItem(position);
        long itemDuration = item.getLength();

        long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                - TimeUnit.MINUTES.toSeconds(minutes);

        holder.tvName.setText(item.getName());
        holder.tvTime.setText(mFormat.format(new Date(item.getTime())) + "  " + String.format("%02d:%02d", minutes, seconds));

        holder.llList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAudioPlayHelper == null) {
                    mAudioPlayHelper = new AudioPlayHelper();
                }
                RecordEntity item = getItem(holder.getLayoutPosition());
                if (item != null) {
                    mAudioPlayHelper.play(item.getPath());
                    mAudioPlayHelper.setAudioPlayChangedListener(new AudioPlayChangedListener() {
                        @Override
                        public void onStart(int totalTime) {
                            holder.llPlay.setVisibility(View.VISIBLE);
                            holder.smList.setVisibility(View.GONE);
                            holder.sbProgress.setMax(totalTime);
                        }

                        @Override
                        public void onChange(int currentTime, int totalTime) {
                            holder.sbProgress.setProgress(currentTime);
                        }

                        @Override
                        public void onStop() {
                            holder.llPlay.setVisibility(View.GONE);
                            holder.smList.setVisibility(View.VISIBLE);
                        }
                    });


                }
            }
        });

        holder.smList.setSlidingMenuTouchListener(new SlidingMenuChangeListener() {
            @Override
            public void onTouch() {
                if (mOpenMenuIndex > -1 && mOpenMenuIndex != holder.getLayoutPosition()) {
                    mSmOpen.close();
                    mOpenMenuIndex = -1;
                    mSmOpen = null;
                }
            }

            @Override
            public void toggle(boolean open) {
                if (open) {
                    mOpenMenuIndex = holder.getLayoutPosition();
                    mSmOpen = holder.smList;
                } else {
                    mOpenMenuIndex = -1;
                    mSmOpen = null;
                }
            }
        });
        holder.smList.close();

        holder.sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mAudioPlayHelper != null && fromUser) {
                    mAudioPlayHelper.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mAudioPlayHelper != null) {
                    mAudioPlayHelper.stopProgress();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mAudioPlayHelper != null) {
                    mAudioPlayHelper.updateProgress();
                }
            }
        });

        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAudioPlayHelper == null) {
                    mAudioPlayHelper = new AudioPlayHelper();
                }
                RecordEntity item = getItem(holder.getLayoutPosition());
                if (item != null) {
                    mAudioPlayHelper.play(item.getPath());
                }
                if (mAudioPlayHelper.isPlaying()) {
                    holder.btnPlay.setBackgroundResource(R.drawable.ic_pause);
                } else {
                    holder.btnPlay.setBackgroundResource(R.drawable.ic_play_fill);
                }
            }
        });

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAudioPlayHelper == null) {
                    mAudioPlayHelper = new AudioPlayHelper();
                }
                RecordEntity item = getItem(holder.getLayoutPosition());
                if (item != null) {
                    mAudioPlayHelper.stopPlaying(item.getPath());
                    remove(holder.getLayoutPosition());
                }

            }
        });
    }


    /**
     * 销毁资源
     */
    public void onDestroy() {
        if (mAudioPlayHelper != null) {
            mAudioPlayHelper.stopPlaying();
        }
        if (mDbHelper != null) {
            mDbHelper.close();
        }

    }

    /**
     * 删除文件
     *
     * @param position 下标
     */
    private void remove(int position) {
        RecordEntity entity = getItem(position);
        if (entity != null) {
            File file = new File(entity.getPath());
            if (file.delete()) {
                mDbHelper.removeRecordById(entity.getId());
                mDatas.remove(position);
                notifyItemRemoved(position);
            }
        }

    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * @param position 下标
     * @return 根据下标获取录音
     */
    public RecordEntity getItem(int position) {
        return mDatas.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SlidingMenu smList;
        LinearLayout llPlay;
        LinearLayout llList;
        TextView tvName;
        TextView tvTime;
        AppCompatSeekBar sbProgress;
        Button btnPlay;
        Button btnDel;


        ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_name);
            tvTime = v.findViewById(R.id.tv_time);
            smList = v.findViewById(R.id.sm_list);
            llPlay = v.findViewById(R.id.ll_play);
            llList = v.findViewById(R.id.ll_list);
            sbProgress = v.findViewById(R.id.sb_progress);
            btnPlay = v.findViewById(R.id.btn_play);
            btnDel = v.findViewById(R.id.btn_del);
        }
    }


}
