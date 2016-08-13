 HSSVM installation and usage: (v1.0.1)

Update history: ==============================================

1. From this version, the version number is normalized to hssvm1.0.1;
2. In this version, we delete the features about running parameter-searching and 
    run-all from Ant script, that is, commands "ant search-param" and "ant run-all" 
    are no longer available.
3. The functions of parameter-searching and run-all are replaced with commands 
    "svm search conf" and "svm runall conf", both of them are used on Linux(all 
    other POSIX system).If you want to use this program on Windows, the cygwin is 
    required to be installed.


Usage:      =================================================

0. Summary
    This program is mainly used for solving multi-class problem in Statistical Learning.
    It use the Hyper-sphere SVM model to complete data training and prediction.Now 
    we only provide C-SVC(C-Support Vector Classification), The following is the 
    detail features:

    1) Check if there are any errors in data file;
    2) Convert data format from index:value to only-value(standard format),or inversely;
    3) Data normalization is implemented; 
    4) Provides parameter optimization feature;
    5) Do training to build hyper-sphere SVM model;
    6) According to exist HSSVM model, do prediction to classify the unknown samples; 
    7) Run all operations in one-step, including: normalization, parameter optimization,
        training, prediction, and report running results.
    8) Execution time can be recorded;    
    9) Provide cache for kernel-matrix to train or predict large-scale sample data.


HSSVM用法说明文档


Update history:＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

1. 自该版本起，版本号规范化为hssvm1.0.1;
2. 删去以前版本中从ant运行参数搜索及run-all功能，相关配置文件一并删除，即:ant search-param
   ant run-all 两命令不再有效； 原ant打jar包功能保留。
3. 改由命令svm search conf, svm runall conf代替，在Linux下运行（windows下可用cygwin）



Usage：＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

0. 概述
    该工具包主要用于解决SVM(支持向量机)多分类问题。利用该工具包，可以对特定格式的样本
    数据、使用SVM超球模型（Hyper-sphere）完成数据的训练与预测。目前本工具包仅支持C_SVC
    (C-Support Vector Classification)，具体功能如下：

    1）可进行样本数据格式的转化，主要是index:value格式向value格式转换，及value格式向index:value
       格式转换；

    2）可对非index:value格式的样本数据进行归一化处理；

    3）可进行参数寻优操作，以使后续训练预测操作达到满意的精度要求；

    4）可根据输入参数及训练样本文件进行训练，建立超球SVM模型；

    5）可根据已建立的超球训练模型，进行数据的分类预测；

    6）可通过命令进行归一化、参数寻优、训练、预测、結果报告等一站式操作。参数
       配置可xml文件里完成；

    7）各关键操作可统计执行时间（如训练、预测、参数寻优、运行所有等时间）；

    8）可进行数据检测是否满足程序处理要求

    9）带缓存机制，可进行大数据量的训练与预测（如样本数据中某最大类样本量超过3000以上）。


QQ交流: 66768544

