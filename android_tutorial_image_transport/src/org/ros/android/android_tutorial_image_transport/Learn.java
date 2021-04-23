package org.ros.android.android_tutorial_image_transport;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

public class Learn extends AbstractNodeMain {
    private String topic_name;
    private String message;

    public Learn() {
        topic_name = "android_learning";
    }

    public Learn(String topic)
    {
        topic_name = topic;
    }

    public void setMessage(String message){
        this.message=message;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android_learning");
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {
        final Publisher<std_msgs.String> publisher =
                connectedNode.newPublisher(topic_name, std_msgs.String._TYPE);
        // This CancellableLoop will be canceled automatically when the node shuts
        // down.
        connectedNode.executeCancellableLoop(new CancellableLoop() {
            private int sequenceNumber;

            @Override
            protected void setup() {
                sequenceNumber = 0;
            }

            @Override
            protected void loop() throws InterruptedException {
                std_msgs.String str = publisher.newMessage();
                str.setData(message);
                publisher.publish(str);
                Thread.sleep(1000);
            }
        });
    }

}