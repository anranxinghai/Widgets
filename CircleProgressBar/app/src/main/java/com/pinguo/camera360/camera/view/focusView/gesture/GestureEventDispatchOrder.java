package com.pinguo.camera360.camera.view.focusView.gesture;

/**
 * Created by wangqinlong on 15-6-8.
 */

/**
 * 以后需要添加GestureListener，请在这里配置各个listener处理
 * 事件的优先级
 * <p/>
 * 优先级值越小的优先级越高，配置的时候切记从优先级最低的往最高的地方按顺序配置
 * 因为enum的初始化对象的顺序是按代码里的顺序来的
 */
public enum GestureEventDispatchOrder {

    // PGCameraPresenter使用
    ZOOM_LISTENER() {
        @Override
        public GestureEventDispatchOrder nextListener() {
            return null;
        }

        @Override
        public GestureEventDispatchOrder lastListener() {
            return GestureEventDispatchOrder.SELFIE_ADJUST_LISTENER;
        }
    },
    SELFIE_ADJUST_LISTENER() {
        @Override
        public GestureEventDispatchOrder nextListener() {
            return ZOOM_LISTENER;
        }

        @Override
        public GestureEventDispatchOrder lastListener() {
            return EFFECT_LISTENER;
        }
    },
    //切换特效使用EffectPresenter
    EFFECT_LISTENER() {
        @Override
        public GestureEventDispatchOrder nextListener() {
            return SELFIE_ADJUST_LISTENER;
        }

        @Override
        public GestureEventDispatchOrder lastListener() {
            return GestureEventDispatchOrder.CAMERA_LISTENER;
        }
    },
    // CameraZoomPresenter使用
    CAMERA_LISTENER() {
        @Override
        public GestureEventDispatchOrder nextListener() {
            return GestureEventDispatchOrder.EFFECT_LISTENER;
        }

        @Override
        public GestureEventDispatchOrder lastListener() {
            return STICKER_SELECT_VIEW_LISTENER;
        }
    },

    STICKER_SELECT_VIEW_LISTENER() {
        @Override
        public GestureEventDispatchOrder nextListener() {
            return CAMERA_LISTENER;
        }

        @Override
        public GestureEventDispatchOrder lastListener() {
            return EFFECT_SELECT_VIEW_LISTENER;
        }
    },

    //SelectViewPresenter使用
    EFFECT_SELECT_VIEW_LISTENER() {
        @Override
        public GestureEventDispatchOrder nextListener() {
            return STICKER_SELECT_VIEW_LISTENER;
        }

        @Override
        public GestureEventDispatchOrder lastListener() {
            return ADVANCE_LISTENER;
        }
    },
    ADVANCE_LISTENER() {
        @Override
        public GestureEventDispatchOrder nextListener() {
            return EFFECT_SELECT_VIEW_LISTENER;
        }

        @Override
        public GestureEventDispatchOrder lastListener() {
            return PREVIEW_SETTING_LISTENER;
        }
    },
    // PreviewSettingsPresenter使用
    PREVIEW_SETTING_LISTENER() {
        @Override
        public GestureEventDispatchOrder nextListener() {
            return ADVANCE_LISTENER;
        }

        @Override
        public GestureEventDispatchOrder lastListener() {
            return null;
        }
    },
    // FrameSettingsPresenter使用
    FRAME_SETTING_LISTENER() {
        @Override
        public GestureEventDispatchOrder nextListener() {
            return PREVIEW_SETTING_LISTENER;
        }

        @Override
        public GestureEventDispatchOrder lastListener() {
            return null;
        }
    };
    protected int mListenerPriority = 0;

    GestureEventDispatchOrder() {
        generateListenerPriority();
    }

    public abstract GestureEventDispatchOrder nextListener();

    public abstract GestureEventDispatchOrder lastListener();

    public void generateListenerPriority() {
        if (nextListener() != null) {
            int priority = nextListener().mListenerPriority;
            this.mListenerPriority = priority - 1;
            return;
        } else if (lastListener() != null) {
            int priority = nextListener().mListenerPriority;
            this.mListenerPriority = priority + 1;
            return;
        }
        this.mListenerPriority = 10;
    }
}
