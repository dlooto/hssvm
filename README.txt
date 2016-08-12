
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


1. Installation and setup
    Before use this program, Java environment is required(java1.6 or above recommended).
      
    1.1 Linux(or other POSIX system)     
        1）Download jdk6 bin package for Linux from http://java.sun.com/javase/downloads/widget/jdk6.jsp
        2）Extract and install the java bin package to some directory（such as, /usr/java/jdk1.6.0_17), 
           and extract hssvm program zip package to a directory（like,/home/john/hssvm1.0.1）. 
        3）Setup environment variable:
            cd to your home directory and edit .bashrc file，enter： gedit .bashrc ,
            add following statements to the end of the file: 

                export JAVA_HOME=/usr/java/jdk1.6.0_17
                export HSSVM_HOME=/home/john/hssvm1.0.1
                export CLASSPATH=.:$JAVA_HOME/lib
                export PATH=$PATH:$JAVA_HOME/bin:$HSSVM_HOME/

           the dir jdk1.6.0_17 is your jdk root dir, HSSVM_HOME is root dir for hssvm program。
        4）Save .bashrc and logout your system, and relogin
        5）open terminal and enter： java -version  , if the version information is showed, 
            java is successfully setup。
        
        If you want to improve source code ,we suggest you install Ant to compile and jar files.
        6）Download Ant binary-release package for Linux from http://ant.apache.org，
            like apache-ant-1.＊-bin.tar.gz
        7）Just like java setup, extract Ant package to a dir（such as, /usr/java/apache-ant-1.7.1），
            add following into .bashrc file：

            export ANT_HOME=/usr/java/apache-ant-1.7.1
            export PATH=$PATH:$JAVA_HOME/bin:$HSSVM_HOME:$ANT_HOME/bin

        8）Save and logout ,enter： ant  , if the following info shows:
            Buildfile: build.xml does not exist!
            Build failed
           Ant installed correctly.
  
    1.2 Windows
        0) If you use HSSVM program on Windows, cygwin is needed, download it from:
            http://www.cygwin.com/, and you can get other help and usage information
            about cygwin
        1）可从 http://java.sun.com/javase/downloads/widget/jdk6.jsp 下载windows版本
           jdk，并安装（安装jdk后一般会默认安装jre，如安装到 E:\java\jdk1.6）
        2）将程序包解压到某目录（如E:\program\hssvm1.0.1）
        3）配置环境变量
            右键单击“我的电脑”属性，进入“高级”， “环境变量”， “新建用户变量”，加入如下变量及值：
            
               变量名                  值            
            JAVA_HOME           E:\java\jdk1.6
            HSSVM_HOME          E:\program\hssvm1.0.1
            PATH                %JAVA_HOME%\bin;%HSSVM_HOME%\
            CLASSPATH           .;%JAVA_HOME%\lib

        4）以上完成后，点“确定”并关闭属性窗口。打开命令行窗口，键入：java -version ，应能看到
            正确的Java环境版本号，

        5) 若要修改代码，建议安装Ant。
           从http://ant.apache.org 下载 Windows版本二进制发布包（如apache-ant-1.＊-bin.zip）
           解压后可放于与jdk相同目录（如E:\java\apache-ant-1.7.1），在环境变量中加入：
                    
                    ANT_HOME=E:\java\apache-ant-1.7.1
                    PATH=%JAVA_HOME%\bin;%HSSVM_HOME%\;%ANT_HOME%\bin
                             
           然后键入ant，出现
                Buildfile: build.xml does not exist!
                Build failed
            则说明Ant已可正常使用。


2. 工具包操作说明
    
    2.0 使用准备

     1) 对所有操作命令，当后面不带任何参数及选项执行时，将会出现命令用法说明。或命令后加 ？ 可达到同样效果。
     2) 将hssvm1.0.1.zip包解压到相关工作目录（如/home/work/hssvm1.0），打开终端(windows平台下打开cygwin)
        切换到数据目录，如 cd /home/work/hssvm1.0.1/data,以下后续各操作都将在该目录下进行。
     3) 键入svm回车，查看可用命令。
          svm checkdata     检查样本数据的格式是否正确
          svm train         数据训练
          svm predict       数据预测
          svm scale         归一化数据
          svm convert       数据格式转换(从index:value到标准格式的互相转换)
          svm search        运行参数寻优
          svm runall        运行所有操作(包括归一化、参数寻优、训练、预测等)
            

    2.1 数据训练(Trainer)======================

    1）用法: svm train [options] train_file [model_file]
    Options:
       -m factor: which determines storing how many rows of kernel matrix in cache.
               It's available only if processing large amount samples(default 0.05)
       -k kernel_type : set type of kernel function (default RBF)
            1 -- polynomial(Unsupported temporaryly) 
            2 -- RBF(radial basis function) 
       -e epsilon : set tolerance of termination criterion (default 0.001)
       -C cost : set the penalty factor C (default 1)
       -g gamma : set gamma in kernel function (default 1)    
    
    2）参数及选项说明
    -m 该选项所设定值仅当多分类问题中某类样本量大于约2500时起作用.样本量大于2500条时，缓存（cache）
       中存储 nxn 核矩阵的行数为：rows = n*factor (n为某类样本量，即样本个数).当样本量小于
       约2500时，采用“上三角矩阵”存储核矩阵（预先存储核矩阵为了加快计算速度）
    -k 核函数类型。当前工具包中仅支持RBF-径向基核函数（此为超球模型中优先选用的核函数）
    -e 训练过程中用SMO算法求解二次规划的终止精度，该值越小，求解耗费时间将越多。

    train_file 
        为“非index:value格式”训练样本文件。文件格式请查看hssvm1.0\data\目录下数据文件示例。
        若您有“index:value格式”数据文件，则需要先将其用 DataTransfer 命令进行转换（命令用法
        见后续介绍）； 一般，为达到更好的预测精度，需对训练数据进行归一化处理（后续介绍）。

    model_file
        训练后建立的超球模型将存入该文件里。该文件名可选，默认文件名为 train_file.model

    3）用法示例
        svm train -C 1.0 -g 2.5  winetrain  wine.model   
            --注意：-C 应为大写,其他参数均为默认。训练过程中，为提高最终预测精度，参数C，gamma
              的选择是关键。所以，实际训练前应考虑进行参数寻优操作（见后续介绍）

        svm train -m 0.1 -C 1.0 -g 4 adult_5000 adult.model
            --若adult_5000为大样本量文件（该样本里存在某类样本量大于约2500个），则此时选项-m将起作
               用。若假设此时其中某类样本有3000，则所建立的缓存将存储 3000*0.1=300 行的核
               矩阵数据。程序运行时存储这些数据需要内存约7.2M


    2.2 数据预测(Predictor)======================

    1）用法：svm predict test_file  model_file [output_file]

    2）参数及选项说明
    test_file
        同样，为得到更好预测精度，应考虑test_file与train_file一起进行归一化处理。
    
    model_file
        为训练过程中生成的模型文件

    output_file
        可选参数。默认文件名为 test_file.out

    3）用法示例：
        svm predict winetest winetrain.model wine.out    
    

    2.3 参数寻优(ParamOptimizer)======================

    1）用法：svm search  [options] train_file 
    Options: 
       -m factor: which determines storing how many rows of kernel matrix in cache.
               It's available only if processing large amount samples(default 0.05)
       -k kernel_type : set type of kernel function (default RBF)
            1 -- polynomial(Unsupported temporaryly) 
            2 -- RBF(radial basis function) 
       -e epsilon : set tolerance of termination criterion (default 0.001)
       -v way: the way of cross-validation(default k-fold cross)
            0 -- Random
            1 -- k-fold cross
            2 -- k-fold cross and random
       -t stype: determines how to choose optimal gamma-C pair (default Minimal C)
            0 -- Minimal C
            1 -- Minimal gamma
            2 -- Both C and gamma are minimal
       -f  fold: the fold number in cross-validation(default 5)
       -C  lower upper step :  set the scope of C(default [1.0 5.0]  step=1.0)
       -g  lower upper step :  set the scope of gamma(default [1.0 5.0]  step=1.0)
       -w  : write parameters searching result to file(no this option, not write)

    2）参数及选项说明
    train_file
        参数寻优过程中，该参数是必需的。通过特定算法，将该数据文件分割训练和预测两部分，由此找到
        合适的gamma-C参数对，作为后面真正训练预测的依据。

    -v 交叉验证方式选择，默认k－重交叉验证方式。目前程序支持三种方式：
       0.随机验证----对训练数据进行分类后，在每一类中选择(1/折数)的样本数，然后将各类所选择的样
         本数合起来作为预测数据，剩余作为训练数据； 
       1.k-重交叉验证----同样在训练数据分好类后，将每一类的样本都分成折数份（如5份），从每一类中
         选择第一个1/5并合起来作为预测数据，剩余作为训练数据。下一次选择每类的第二个1／5合起来作
         为预测数据， 以此循环，直到各类中的每一份数据都作为预测数据一次。
         此方式比前一种需多出折数次的循环，故需更多寻优时间。但得到的参数結果也更稳定，精度也较高。
       2.k-重交叉随机验证----此方式综合以上两种，所花时间与k-重交叉验证同。在样本数最大的一类中
         顺序选取一折（如1/5），而在其他类中随机选取一折，合起来作为预测数据。 

    -t 在寻优結果中，一般选取精度最高結果作为后续训练预测，因存在许多同样精度值的不同gamma-C对，所
       以需考虑使用何种策略来决定哪些参数对为最优，目前支持以下三种策略：
        0.最小C 
        1.最小gamma
        2.C、gamma均最小   

    -f 交叉验证折数，与-v选项配合使用。该值不建议小于3

    -C 寻优中的C的范围，由［上界，下界］及步长组成。 上下界间距越大及步长越小，耗费时间越多，但找到
       更优参数对的可能性更大。

    -g 类似C的范围选取。
    -w 寻优过程将产生“各种不同参数对”进行训练预测的結果，加上该选项，这些結果将被写入文件，可用于
       其他工具绘制等高线。 注，该选项后无需参数值。

    3）用法示例
        svm search -v 0 -t 2 -f 3 -C 0.5 5 0.5 -g 0.5 5 0.5 -w winetrain
        svm search  winetrain
            
      因该命令参数较多，而除train_file外，其他选项都有默认值。若要修改选项，推荐以：
        svm search conf  
      方式运行，后续将作介绍。
        
        

    2.4 归一化处理(Normalizer)======================

    1）用法: svm scale [option] train_file  test_file 
         option: -s lower upper (default[-1.0 1.0]）

    2）选项及参数说明
       正式训练预测时，一般要求对训练样本文件和预测样本文件同时进行归一化，因而此时参数train_file,
       test_file都需要提供。但若只需对某单个样本文件进行（如参数寻优过程中只处理训练样本文件），
       此时可不提供test_file参数。

       -s lower upper:  归一化上下界

    3）用法示例
        svm scale -s 0.1 0.9 winetrain winetest 

        svm scale winetrain 


    2.5 数据格式转换(DataTransfer)======================

    1）用法: svm convert [option] src_file [dest_file] 
        -i : convert non-index format data to index format data(if no this option,
             convert the index format to non-index format)

    2）选项及参数说明
      index格式数据，形如： 
        -1 1:2.5  2:1.3  3:2.0       
        +1 1:2.5  2:1.3  3:3.0 

      non-index格式数据，形如：    
        -1  2.5  1.3  2.0       
        +1  2.5  1.3  3.0 

       本工具包只处理non-index格式数据，所以，若您的数据为index格式，则需要用此“数据格式转换”
      命令将其转成non-index格式，以便程序处理。

    -i 执行命令时，若无此选项，则程序将默认src_file为index格式数据文件，转换后将变成non-index
       格式文件，默认文件名为src_file.n。若加上此选项，则反之，将src_file由non-index格式转成
       index格式（该功能可为其他工具包提供处理数据）          

    3）用法示例
        svm convert winetrain.index  ----結果文件为winetrain.index.n， non-index格式
        svm convert -i winetrain     ----結果文件为winetrain.i，index格式


3. 批量处理及参数配置 
       
    3.0 运行参数寻优或runall时，通过hssvm-config.xml配置参数，为推荐方式。
 
    上述各操作（归一化、参数寻优、训练、预测等）逐一进行可能要经历一番繁琐复杂的过程，且当有的命令
    存在较多参数时，参数的输入、挑选、格式的正确与否等都会让操作变得复杂、易于出错。基于此，本程序提
    供了通过文件进行参数配置，可使所有操作较方便地一步完成。

    找到工作目录下的hssvm-config.xml文件，在该文件里配置了所有程序运行所需的参数。更改后保存该
    文件，运行search或runall即可。

   3.1 命令使用
    svm search conf

    svm runall conf    
    
    注： conf为一特定标识，表示通过hssvm-config.xml文件读取配置参数。若键入命令时无此一标识，
    则各参数仍然从命令行读取。


4. 其他
有任何问题或意见，请联系：
    wangkai73@sina.com， tangxb200@126.com， xjbean@gmail.com    
         
