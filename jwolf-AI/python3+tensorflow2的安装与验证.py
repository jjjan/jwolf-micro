"""
1. 官网https://www.python.org/downloads/windows/选择python3.7+ excutable installer进行安装,这里使用3.8.1，设置环境变量
2. tensorflow底层c++,需要安装适合的visual C++版本，如使用2.x版本tensorflow 安装VC++2015会缺少必要的dll文件 ，这里安装visualc++2019 https://aka.ms/vs/16/release/VC_redist.x64.exe,其它版本参考  https://blog.csdn.net/kpacnB_Z/article/details/110122336
3. 使用清华pip源:pip config set global.index-url https://pypi.tuna.tsinghua.edu.cn/simple
4. pip install tensorflow jupyter numpy pandas 等,tensorflow_gpu为对应gpu版本，这里使用2.7.0,2.X与1.X有很大版本差异;jupyter 安装后可以在IDEA运行jupyter notebook,详细参考doc/doc-resource/IDEA-jupyter.png
"""


import os
import tensorflow as tf


def test():
    os.system('python -V')
    os.system(tf.__version__)
    os.system('pip -V')
    os.system('lscpu')


if __name__ == '__main__':
    test()


