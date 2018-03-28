package com.dyhdyh.smartpay;

import android.app.Activity;

import com.dyhdyh.smartpay.subscriber.DefaultResultSubscriber;

import java.util.Map;

/**
 * @author dengyuhan
 *         created 2018/3/27 17:16
 */
public class SmartPay {
    private Activity mActivity;
    private PayType mPayType;
    private SmartPayCallFactory mCallFactory;
    private SmartPaymentParamsFactory mParamsFactory;
    /**
     * 额外的参数
     */
    private Map<String, Object> mExtras;

    private static SmartPay mInstance;

    private SmartPay(Activity activity) {
        this.mActivity = activity;
        this.callFactory(new DefaultCallFactory(mActivity));
        this.converterFactory(new DefaultConverterFactory());
    }

    public static SmartPay with(Activity activity) {
        synchronized (SmartPay.class) {
            if (mInstance == null) {
                mInstance = new SmartPay(activity);
            }
        }
        return mInstance;
    }

    public SmartPay payType(PayType payType) {
        this.mPayType = payType;
        return this;
    }

    public SmartPay callFactory(SmartPayCallFactory factory) {
        if (factory == null) {
            return this;
        }
        this.mCallFactory = factory;
        return this;
    }


    public SmartPay converterFactory(SmartPayConverterFactory factory) {
        SmartPayResultObserver.setConverterFactory(factory);
        return this;
    }

    public SmartPay params(SmartPaymentParamsFactory factory) {
        this.mParamsFactory = factory;
        return this;
    }

    /**
     * 附加额外的参数,该参数会原样返回到startPay
     *
     * @param extras
     * @return
     */
    public SmartPay extras(Map<String, Object> extras) {
        this.mExtras = extras;
        return this;
    }

    public SmartPay setOnPaymentListener(OnSmartPaymentListener listener) {
        SmartPayResultObserver.register(new DefaultResultSubscriber(listener));
        return this;
    }

    public <T> T asObservable2(SmartPayCallAdapter<T> adapter) {
        return (T) mCallFactory.create(mPayType).call(mParamsFactory.build(mPayType), mExtras);
    }

    public void start() {
        mCallFactory.create(mPayType).call(mParamsFactory.build(mPayType), mExtras);
    }

}