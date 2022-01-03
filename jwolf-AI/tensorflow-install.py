"""
1. tensorflow底层c++,需要安装visual C++ 2015或其它版本,https://www.microsoft.com/en-us/download/confirmation.aspx?id=48145
2. 使用清华pip源:pip config set global.index-url https://pypi.tuna.tsinghua.edu.cn/simple
3. python3.8+只能安装tensorflow2+??,GPU版本tensorfow_gpu:python降为3.6安装tensorflow1,pip install tensorflow===1.5.1 -i  https://pypi.tuna.tsinghua.edu.cn/simple
4. 因为dev pc比较旧或visual C++比较低，使用2.x版本tensorflow会缺少必要的dll文件
"""


import os
import tensorflow as tf

def testTensorflow():
    hello = tf.constant('Hello,TensorFlow')
    with tf.Session() as sess:
         print(sess.run(hello))

if __name__ == '__main__':
    os.system('python -V')
    os.system(tf.VERSION)
    os.system('echo 11')
    os.system('pwd')
    os.system('lscpu')
    testTensorflow()


