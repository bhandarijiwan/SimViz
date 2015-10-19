/*
 * Copyright 2014 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.visimulation.simviz;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Contains two sub-views to provide a simple stereo HUD.
 */
public class CardboardOverlayView extends LinearLayout {
 //   private static final String TAG = CardboardOverlayView.class.getSimpleName();
    private final CardboardOverlayEyeView mLeftView;
    private final CardboardOverlayEyeView mRightView;
    private AlphaAnimation mTextFadeAnimation;

    public CardboardOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

        LayoutParams params = new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(0, 0, 0, 0);

        mLeftView = new CardboardOverlayEyeView(context, attrs);
        mLeftView.setLayoutParams(params);
        addView(mLeftView);

        mRightView = new CardboardOverlayEyeView(context, attrs);
        mRightView.setLayoutParams(params);
        addView(mRightView);

        // Set some reasonable defaults.
        
        setColor(Color.rgb(0, 255, 0));
        setVisibility(View.VISIBLE);

        mTextFadeAnimation = new AlphaAnimation(1.0f, 0.0f);
        
    }
    
    public void show3DToast(String message,long timeDuration){
    	setText(message);
        setTextAlpha(1f);
        mTextFadeAnimation.setDuration(timeDuration);
        mTextFadeAnimation.setAnimationListener(new EndAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                setTextAlpha(0f);
            }
        });
        startAnimation(mTextFadeAnimation);
    }
    public void show3DToast(String message) {
        setText(message);
        setTextAlpha(1f);
        mTextFadeAnimation.setAnimationListener(new EndAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                setTextAlpha(0f);
            }
        });
        startAnimation(mTextFadeAnimation);
    }

    private abstract class EndAnimationListener implements Animation.AnimationListener {
        @Override public void onAnimationRepeat(Animation animation) {}
        @Override public void onAnimationStart(Animation animation) {}
    }

   
    private void setText(String text) {
        mLeftView.setText(text);
        mRightView.setText(text);
    }

    private void setTextAlpha(float alpha) {
        mLeftView.setTextViewAlpha(alpha);
        mRightView.setTextViewAlpha(alpha);
    }

    private void setColor(int color) {
        mLeftView.setColor(color);
        mRightView.setColor(color);
    }
  
    /**
     * A simple view group containing some horizontally centered text underneath a horizontally
     * centered image.
     * This is a helper class for CardboardOverlayView.
     */
    private class CardboardOverlayEyeView extends ViewGroup {
        private final TextView outLineTextView;
        
        // outline for the 3D Toast.
        
        private class OutLineTextView extends TextView {
        	
        	public OutLineTextView(Context context) {
				super(context);
			//	this.setupPaint();
			}
        	public void draw(Canvas canvas){
        			int textColor = getTextColors().getDefaultColor();
        		    setTextColor(Color.BLACK); // your stroke's color
        		    getPaint().setStrokeWidth(10);
        		    getPaint().setStyle(Paint.Style.STROKE);
        		    super.draw(canvas);
        		    setTextColor(textColor);
        		    getPaint().setStrokeWidth(0);
        		    getPaint().setStyle(Paint.Style.FILL);
        		    super.draw(canvas);
        		
        	}

        	
        };
        
        
        public CardboardOverlayEyeView(Context context, AttributeSet attrs) {
            super(context, attrs);
            
            outLineTextView = new OutLineTextView(context);
            outLineTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16.0f);
            outLineTextView.setTypeface(outLineTextView.getTypeface(), Typeface.BOLD);
            outLineTextView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
            outLineTextView.setPadding(10, 10, 10, 0);
            outLineTextView.setTextColor(Color.GREEN);
            addView(outLineTextView);
           
        }

        public void setColor(int color) {
        	outLineTextView.setTextColor(color);
        }

        public void setText(String text) {
            
            outLineTextView.setText(text);
            
        }

        public void setTextViewAlpha(float alpha) {
            outLineTextView.setAlpha(alpha);
        }
        
        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            // Width and height of this ViewGroup.
            final int width = right - left;
            final int height = bottom - top;
            outLineTextView.layout((int)(0.2*width), (int)(0.4*height), (int) (0.8*width), (int)(0.7*height));
        }
    }
}
