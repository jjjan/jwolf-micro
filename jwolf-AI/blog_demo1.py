import tensorflow as tf

# ��ȡ����,MNIST���ݼ�����55000������ѵ������5000��������֤����10000�����Ĳ��Լ�
from tensorflow.examples.tutorials.mnist import input_data
mnist = input_data.read_data_sets("input_data", one_hot=True)

# ��ʾͼ���������״
print('ѵ������Ϣ��')
print(mnist.train.images.shape,mnist.train.labels.shape)
print('���Լ���Ϣ��')
print(mnist.test.images.shape,mnist.test.labels.shape)
print('��֤����Ϣ��')
print(mnist.validation.images.shape,mnist.validation.labels.shape)

# ʵ��ģ�� y=softmax(wx+b)
# placeholder���������ݵĵط���None ���������������룬ÿ����784ά������
# Variable���洢ģ�Ͳ������־û���
sess = tf.InteractiveSession()
x = tf.placeholder(tf.float32, [None, 784])
W = tf.Variable(tf.zeros([784,10]))
b = tf.Variable(tf.zeros([10]))
y = tf.nn.softmax(tf.matmul(x,W) + b)

# ����һ����������Ϊloss����cross_entropy,����y������Ԥ��ĸ��ʷֲ�, y_��ʵ�ʵķֲ�
y_ = tf.placeholder(tf.float32, [None,10])
cross_entropy = tf.reduce_mean(-tf.reduce_sum(y_ * tf.log(y),reduction_indices=[1]))

# ��������ݶ��½���������Ϊ0.5����ѵ��
train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)
# ��ģ��ѭ��ѵ��1000�Σ�ÿ�����train100������
tf.global_variables_initializer().run()
for i in range(1000):
  batch_xs, batch_ys = mnist.train.next_batch(100)
  train_step.run({x: batch_xs, y_: batch_ys})

# ģ������
correct_prediction = tf.equal(tf.argmax(y,1), tf.argmax(y_,1))
accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
print('MNIST��дͼƬ׼ȷ�ʣ�')
print(accuracy.eval({x: mnist.test.images, y_: mnist.test.labels}))
