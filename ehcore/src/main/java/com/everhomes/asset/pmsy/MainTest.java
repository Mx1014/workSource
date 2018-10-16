package com.everhomes.asset.pmsy;

/**
 * Created by Administrator on 2017/7/18.
 */
public class MainTest {

        /**
         * @param args
         */
        public static void main(String[] args) {
            EventSourceObject object = new EventSourceObject();
            //注册监听器
            object.addCusListener(new PersonelEventListener(){
                @Override
                public void handleEvent(PersonelEvent e) {
                    super.handleEvent(e);
                }
            });
            //触发事件
            object.setName("eric");
        }
}
