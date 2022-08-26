package com.jiuyue.user.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemBusyDateBinding
import com.jiuyue.user.databinding.ItemBusyTimeBinding
import com.jiuyue.user.entity.ListBean
import com.jiuyue.user.entity.ReserveTimeEntity
import com.jiuyue.user.mvp.model.CommonModel
import com.jiuyue.user.net.ResultListener
import com.jiuyue.user.utils.ScreenUtils
import com.jiuyue.user.utils.ToastUtil
import com.lxj.xpopup.core.BottomPopupView
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class ChooseReserveTimePopup : BottomPopupView {
    private var mContext: Context
    private var mTechId: Int = -1
    private lateinit var tvEmpty: AppCompatTextView

    private var buttonClickListener: OnButtonClickListener? = null

    interface OnButtonClickListener {
        fun onConfirm(data: ReserveTimeEntity)
    }

    constructor(context: Context) : super(context) {
        this.mContext = context
    }

    constructor(
        context: Context,
        techId: Int,
        buttonClickListener: (data: ReserveTimeEntity) -> Unit
    ) : super(context) {
        this.mContext = context
        this.mTechId = techId
        this.buttonClickListener = object : OnButtonClickListener {
            override fun onConfirm(data: ReserveTimeEntity) {
                buttonClickListener(data)
            }
        }
    }

    private val mDateAdapter by lazy {
        ReserveDateAdapter().apply {
            setOnItemClickListener { _, _, position ->
                setCurPosition(position)
                //更新时间列表
                mTimeAdapter.setList(data[position].times)
                mTimeAdapter.setCurPosition(-1)
            }
        }
    }

    private val mTimeAdapter by lazy {
        ReserveTimeAdapter().apply {
            setOnItemClickListener { _, _, position ->
                setCurPosition(position)
            }
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.popup_reserve_time
    }

    override fun getMaxHeight(): Int {
        return 0
    }

    override fun onCreate() {
        super.onCreate()
        val rvDate = findViewById<RecyclerView>(R.id.rv_date)
        val rvTime = findViewById<RecyclerView>(R.id.rv_time)
        val btnConfirm = findViewById<AppCompatButton>(R.id.btn_confirm)
        tvEmpty = findViewById<AppCompatTextView>(R.id.tv_empty)
        rvDate.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        rvDate.adapter = mDateAdapter
        rvTime.layoutManager = GridLayoutManager(mContext, 4)
        rvTime.adapter = mTimeAdapter

        btnConfirm.setOnClickListener {
            dismiss()
            if (mTimeAdapter.getCurData() != null) {
                val data = mDateAdapter.getCurData()
                data.times.clear()
                data.times.add(mTimeAdapter.getCurData())
                buttonClickListener!!.onConfirm(data)
            } else {
                ToastUtil.show("您未选择时间")
            }
        }

    }

    override fun beforeShow() {
        super.beforeShow()
        CommonModel().technicianServiceTimeList(mTechId,
            object : ResultListener<List<ReserveTimeEntity>> {
                override fun onSuccess(data: List<ReserveTimeEntity>) {
                    if (data.isNotEmpty()) {
                        tvEmpty.visibility = View.GONE
                        mDateAdapter.setList(data)
                        mDateAdapter.setCurPosition(0)
                        mTimeAdapter.setList(data[0].times)
                    } else {
                        tvEmpty.visibility = View.VISIBLE
                    }
                }

                override fun onError(msg: String?, code: Int) {
                    ToastUtil.show(msg)
                    tvEmpty.visibility = View.VISIBLE
                }
            })
    }

    class ReserveDateAdapter : BaseBindingAdapter<ReserveTimeEntity, ItemBusyDateBinding>(0) {
        private var curPosition = 0//当前位置
        private var prePosition = 0//上一个位置
        override fun convert(
            holder: BaseVBViewHolder<ItemBusyDateBinding>,
            item: ReserveTimeEntity
        ) {
            holder.bd.tvItemBusyDate.text = item.dateTitle
            holder.bd.tvItemBusyWeek.text = item.weekDay
            if (curPosition == holder.bindingAdapterPosition) {
                prePosition = curPosition
                holder.bd.tvItemBusyDate.setTextColor(
                    ContextCompat.getColor(
                        App.getAppContext(),
                        R.color.color333
                    )
                )
                holder.bd.tvItemBusyWeek.setTextColor(
                    ContextCompat.getColor(
                        App.getAppContext(),
                        R.color.color333
                    )
                )
                holder.bd.itemIndicator.visibility = View.VISIBLE
            } else {
                holder.bd.tvItemBusyDate.setTextColor(
                    ContextCompat.getColor(
                        App.getAppContext(),
                        R.color.color999
                    )
                )
                holder.bd.tvItemBusyWeek.setTextColor(
                    ContextCompat.getColor(
                        App.getAppContext(),
                        R.color.color999
                    )
                )
                holder.bd.itemIndicator.visibility = View.INVISIBLE
            }
        }

        fun setCurPosition(sel: Int) {
            this.curPosition = sel
            notifyItemChanged(curPosition)
            notifyItemChanged(prePosition)
        }

        fun getCurData(): ReserveTimeEntity {
            return data[curPosition]
        }
    }

    class ReserveTimeAdapter :
        BaseBindingAdapter<ReserveTimeEntity.TimesDTO, ItemBusyTimeBinding>(0) {
        private var curPosition = -1//当前位置
        private var prePosition = -1//上一个位置
        override fun convert(
            holder: BaseVBViewHolder<ItemBusyTimeBinding>,
            item: ReserveTimeEntity.TimesDTO
        ) {
            holder.bd.tvItemBusyPeriod.text = item.time
            if (item.status == 0) {
                setDisabled(holder)
            } else {
                if (curPosition == holder.bindingAdapterPosition) {
                    prePosition = curPosition
                    setChecked(holder)
                } else {
                    setUnCheck(holder)
                }
            }
        }

        /**
         * 设置当前选中
         *
         * @param sel
         */
        fun setCurPosition(sel: Int) {
            this.curPosition = sel
            notifyItemChanged(curPosition)
            notifyItemChanged(prePosition)
        }

        fun getCurData(): ReserveTimeEntity.TimesDTO? {
            if (curPosition == -1) {
                return null
            }
            return data[curPosition]
        }

        /**
         * 设置选中
         *
         * @param holder
         */
        private fun setChecked(holder: BaseVBViewHolder<ItemBusyTimeBinding>) {
            holder.bd.rlContainer.background = ContextCompat.getDrawable(
                App.getAppContext(),
                R.drawable.shape_fbe8ef_radius_50dp
            )
            holder.bd.tvItemBusyPeriod.setTextColor(
                ContextCompat.getColor(
                    App.getAppContext(),
                    R.color.colorPrice
                )
            )
            holder.bd.ivItemBusyLabel.visibility = View.GONE
        }

        /**
         * 取消选中
         *
         * @param holder
         */
        private fun setUnCheck(holder: BaseVBViewHolder<ItemBusyTimeBinding>) {
            holder.bd.rlContainer.background = ContextCompat.getDrawable(
                App.getAppContext(),
                R.drawable.stroke_1dp_bbb_50dp
            )
            holder.bd.tvItemBusyPeriod.setTextColor(
                ContextCompat.getColor(
                    App.getAppContext(),
                    R.color.color333
                )
            )
            holder.bd.ivItemBusyLabel.visibility = View.GONE
        }

        /**
         * 设置禁用
         *
         * @param holder
         */
        private fun setDisabled(holder: BaseVBViewHolder<ItemBusyTimeBinding>) {
            holder.bd.rlContainer.background = ContextCompat.getDrawable(
                App.getAppContext(),
                R.drawable.stroke_1dp_bbb_50dp
            )
            holder.bd.tvItemBusyPeriod.setTextColor(
                ContextCompat.getColor(
                    App.getAppContext(),
                    R.color.color999
                )
            )
            holder.bd.ivItemBusyLabel.visibility = View.VISIBLE
        }
    }
}