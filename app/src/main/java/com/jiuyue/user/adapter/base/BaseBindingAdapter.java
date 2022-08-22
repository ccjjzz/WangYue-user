package com.jiuyue.user.adapter.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 基于BRVAH封装的支持ViewBinding的适配器
 *
 * @param <T>
 * @param <VB>
 */
public abstract class BaseBindingAdapter<T, VB extends ViewBinding> extends BaseQuickAdapter<T, BaseBindingAdapter.BaseVBViewHolder<VB>> {
    private Class<?> vbClass;

    // public VB bd;
    public BaseBindingAdapter(int subView) {
        super(subView);
        initVBClass();
    }

    public BaseBindingAdapter(int subView, @Nullable List<T> data) {
        super(subView, data);
        initVBClass();
    }

    private void initVBClass() {
        Type superclass = getClass().getGenericSuperclass();
        Type[] typeArr = ((ParameterizedType) superclass).getActualTypeArguments();
        for (Type type : typeArr) {
            Class<?> aClass = (Class<?>) type;
            if (ViewBinding.class.isAssignableFrom(aClass)) {
                vbClass = aClass;
                return;
            }
        }
        throw new RuntimeException("你的适配器需要提供一个ViewBinding的泛型才能进行视图绑定");
    }

    @NotNull
    @Override
    protected BaseVBViewHolder<VB> onCreateDefViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new BaseVBViewHolder<VB>(getViewBinding(LayoutInflater.from(parent.getContext()), parent));
    }

    @SuppressWarnings("unchecked")
    protected VB getViewBinding(LayoutInflater from, ViewGroup parent) {
        VB binding = null;
        try {
            Method method = vbClass.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            binding = (VB) method.invoke(null, from, parent, false);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return binding;
    }

    @SuppressWarnings("unchecked")
    public static class BaseVBViewHolder<VB> extends BaseViewHolder {
        public VB bd;

        public <B extends ViewBinding> BaseVBViewHolder(B binding) {
            super(binding.getRoot());
            this.bd = (VB) binding;
        }
    }
}