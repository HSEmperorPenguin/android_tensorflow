/*
 * Copyright (C) 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ros.android.android_tutorial_image_transport;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import org.ros.address.InetAddressFactory;
import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.RosActivity;
import org.ros.android.view.RosImageView;

import org.ros.android.view.RosTextView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;


/**
 * @author ethan.rublee@gmail.com (Ethan Rublee)
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MainActivity extends RosActivity {

  private RosImageView<sensor_msgs.CompressedImage> image;
  private  RosTextView<java.lang.String> car_learning;
  private Button start_button,learning_button;
  private ImageView handle,blur;
  private Learn learn;
  private Start start;
  public MainActivity() {
    super("ImageTransportTutorial", "ImageTransportTutorial");
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    image = (RosImageView<sensor_msgs.CompressedImage>) findViewById(R.id.image);
    image.setTopicName("/usb_cam/image_raw/compressed");
    image.setMessageType(sensor_msgs.CompressedImage._TYPE);
    image.setMessageToBitmapCallable(new BitmapFromCompressedImage());

    learning_button=(Button)findViewById(R.id.btn_learning);
    learning_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        learn.setMessage("True");
      }
    });

    start_button=(Button)findViewById(R.id.btn_start);
    start_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        start.setMessage("True");
      }
    });
  }

  @Override
  protected void init(NodeMainExecutor nodeMainExecutor) {

    learn = new Learn("android_learning");
    learn.setMessage("Flase");

    start = new Start("android_starting");
    start.setMessage("Flase");

    NodeConfiguration nodeConfiguration =
            NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress(),
                    getMasterUri());

    nodeMainExecutor.execute(learn, nodeConfiguration);
    nodeMainExecutor.execute(start, nodeConfiguration);
    nodeMainExecutor.execute(image, nodeConfiguration.setNodeName("android/video_view"));
  }
}


