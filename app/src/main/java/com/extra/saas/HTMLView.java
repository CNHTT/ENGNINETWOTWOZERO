package com.extra.saas;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 戴尔 on 2017/11/17.
 */
@SuppressLint("AppCompatCustomView")
public class HTMLView extends TextView {

    private static final String TAG = "HtmlTextView";
    private Context context;
    public static final int WHAT_LOAD_COMPLETE = 200;
    public HTMLView(Context context) {
        super(context);
        this.context = context;
    }

    public HTMLView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public HTMLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setHtmlText(String htmlText) {
        if (htmlText.startsWith("<html>")) {
            html = htmlText;
            new Thread(runnable).start();
        } else {
            setText(htmlText);
        }

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_LOAD_COMPLETE:
                    CharSequence article = (CharSequence) msg.obj;
                    setText(article);

                    break;

            }
        }
    };


    private String html;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {


            /**
             * 要实现图片的显示需要使用Html.fromHtml的一个重构方法：public static Spanned
             * fromHtml (String source, Html.ImageGetterimageGetter,
             * Html.TagHandler
             * tagHandler)其中Html.ImageGetter是一个接口，我们要实现此接口，在它的getDrawable
             * (String source)方法中返回图片的Drawable对象才可以。
             */
            Html.ImageGetter imageGetter = new Html.ImageGetter() {

                @Override
                public Drawable getDrawable(String source) {
                    URL url;
                    Drawable drawable = null;
                    try {
                        url = new URL(source);
                        drawable = Drawable.createFromStream(
                                url.openStream(), null);
                        drawable.setBounds(0, 0,
                                drawable.getIntrinsicWidth(),
                                drawable.getIntrinsicHeight());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return drawable;
                }
            };

            CharSequence article = Html.fromHtml(html, imageGetter, null);
            Message msg = Message.obtain();
            msg.what = WHAT_LOAD_COMPLETE;
            msg.obj = article;
            handler.sendMessage(msg);

        }
    };
}