from sqlalchemy import create_engine, Column, Integer, Float, String, DateTime, TIMESTAMP, Double, Text, BLOB
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from sqlalchemy_utils import database_exists, create_database, drop_database
from datetime import datetime, timedelta
import time
import random

# python2: MySQLdb
# python3: pymysql
# 使用create_engine建立同数据库的连接，返回的是一个Engine实例
# 指向数据库的一些核心的接口
# echo=True， 可以在控制台看到操作涉及的SQL语言

username = "root"
password = "azx"
database = "ai_wiki"

# session = Session()
# 声明基类
Base = declarative_base()
# 基于这个基类来创建我们的自定义类，一个类就是一个数据库表


class ai_qa_history(Base):
    __tablename__ = 'ai_qa_history'
    qahid = Column(Integer, nullable=False,
                   autoincrement=True, primary_key=True)
    uid = Column(Integer, nullable=False)
    question = Column(Text, nullable=False)
    answer = Column(Text)
    questionTime = Column(DateTime, nullable=False)
    answerTime = Column(DateTime, nullable=False)

    def __repr__(self):
        return self.qahid


class entry(Base):
    __tablename__ = 'entry'
    eid = Column(Integer, nullable=False, primary_key=True)
    title = Column(String(100), nullable=False)
    content = Column(Text, nullable=True)
    description = Column(String(256), nullable=True)
    category = Column(String(30), nullable=True)
    image = Column(String(30), nullable=True)
    visits = Column(Integer, default=0)
    likes = Column(Integer, default=0)
    favors = Column(Integer, default=0)

    def __repr__(self):
        return self.eid

    def __init__(self, eid, title, content="", description="", category="", image="", visits=0, likes=0, favors=0):
        self.eid = eid
        self.title = title
        self.content = content
        self.description = description
        self.category = category
        self.image = image
        self.visits = visits
        self.likes = likes
        self.favors = favors


class entry_submission(Base):
    __tablename__ = 'entry_submission'
    esid = Column(Integer, nullable=False,
                  autoincrement=True, primary_key=True)
    uid = Column(Integer, nullable=False)
    eid = Column(Integer, nullable=False)
    title = Column(String(100), nullable=False)
    content = Column(Text, nullable=False)
    description = Column(String(256), nullable=True)
    category = Column(String(30), nullable=True)
    image = Column(String(30), nullable=True)
    submit_time = Column(DateTime, nullable=False)
    status = Column(String(10), nullable=False)
    admin = Column(Integer, nullable=False)

    def __init__(self, eid, uid, title, category, content="", status="待处理", submit_time=None, admin=1):
            self.eid = eid
            self.uid = uid
            self.title = title
            self.content = content
            self.category = category
            self.status = status
            # 随机生成提交时间（示例：当前时间减去0到10天）
            self.submit_time = submit_time if submit_time else datetime.now() - timedelta(days=random.randint(0, 10))
            self.admin = admin

    def __repr__(self):
            return f"entry(eid={self.eid}, title='{self.title}', category='{self.category}', content='{self.content}', status='{self.status}', submitTime='{self.submitTime}', admin={self.admin})"


class entry_submission_review(Base):
    __tablename__ = 'entry_submission_review'
    esrid = Column(Integer, autoincrement=True, primary_key=True)
    admin = Column(Integer, nullable=False)
    esid = Column(Integer, nullable=False)
    status = Column(String(10), nullable=False)
    comment = Column(Text, nullable=True)
    review_time = Column(DateTime, nullable=False)

class error_correction(Base):
    __tablename__ = 'error_correction'
    ecid = Column(Integer, nullable=False,
                  autoincrement=True, primary_key=True)
    uid = Column(Integer, nullable=False)
    eid = Column(Integer, nullable=False)
    title = Column(String(100), nullable=False)
    content = Column(Text, nullable=False)
    description = Column(String(256), nullable=True)
    category = Column(String(30), nullable=True)
    image = Column(String(30), nullable=True)
    submit_time = Column(DateTime, nullable=False)
    status = Column(String(10), nullable=False)
    admin = Column(Integer, nullable=False)

    def __init__(self, eid, uid, title, content="", status="待处理", submit_time=None, admin=1):
                self.eid = eid
                self.uid = uid
                self.eid = eid
                self.title = title
                self.content = content
                self.status = status
                # 随机生成提交时间（示例：当前时间减去0到10天）
                self.submit_time = submit_time if submit_time else datetime.now() - timedelta(days=random.randint(0, 10))
                self.admin = admin

class error_correction_review(Base):
    __tablename__ = 'error_correction_review'
    ecrid = Column(Integer, autoincrement=True, primary_key=True)
    admin = Column(Integer, nullable=False)
    ecid = Column(Integer, nullable=False)
    status = Column(String(10), nullable=False)
    comment = Column(Text, nullable=True)
    review_time = Column(DateTime, nullable=False)



class entry_tag(Base):
    __tablename__ = 'entry_tag'
    etid = Column(Integer, autoincrement=True, primary_key=True)
    eid = Column(Integer, nullable=False)
    tag = Column(String(30), nullable=False)


class search_history(Base):
    __tablename__ = 'search_history'
    shid = Column(Integer, autoincrement=True, primary_key=True)
    uid = Column(Integer, nullable=False)
    content = Column(Text, nullable=False)
    searchTime = Column(DateTime, nullable=False)


class tool(Base):
    __tablename__ = 'tool'
    tid = Column(Integer, primary_key=True)
    title = Column(String(45), nullable=False)
    href = Column(String(256), nullable=False)
    description = Column(String(256), nullable=False)
    category = Column(String(45), nullable=False)
    recommend = Column(Integer, nullable=False)

    def __init__(self, tid, title, herf, description, category, recommend=1):
        self.eid = tid
        self.title = title
        self.herf = herf
        self.description = description
        self.category = category
        self.recommend = recommend


class user(Base):
    __tablename__ = 'user'
    uid = Column(Integer, autoincrement=True, primary_key=True)
    username = Column(String(10), nullable=False)
    password = Column(String(20), nullable=False)
    email = Column(String(30), nullable=False)
    role = Column(Integer, nullable=False)
    points = Column(Integer, nullable=True)
    level = Column(Integer, nullable=True)
    name = Column(String(30), nullable=True)
    gender = Column(String(1), nullable=True)
    phone = Column(String(20), nullable=True)
    birth = Column(DateTime, nullable=True)
    image = Column(String(256), nullable=True)
    description = Column(String(200), nullable=True)

    def __init__(self, username, password, email, role="", points=0, level=0, name="", gender="", phone="", birth=None, image="", description=""):
        self.username = username
        self.password = password
        self.email = email
        self.role = role
        self.points = points
        self.level = level
        self.name = name
        self.gender = gender
        self.phone = phone
        self.image = image
        self.birth = birth
        self.image = image
        self.description = description


class user_entry(Base):
    __tablename__ = 'user_entry'
    ueid = Column(Integer, autoincrement=True, primary_key=True)
    uid = Column(Integer, nullable=False)
    eid = Column(String(10), nullable=False)
    type = Column(String(15), nullable=False)

    def __init__(self, uid, eid, type):
        self.uid = uid
        self.eid = eid
        self.type = type


class user_tag(Base):
    __tablename__ = 'user_tag'
    utid = Column(Integer, autoincrement=True, primary_key=True)
    utname = Column(String(10), nullable=False)
    uid = Column(Integer, nullable=False)
    relevance = Column(Double, nullable=False)


# 新增函数，若有旧的数据库则删除，没有则创建新的数据库，便于修改表结构
def drop_and_create_database(engine):
    if database_exists(engine.url):
        print(f"Deleting database {database}")
        drop_database(engine.url)
    print(f"Creating database {database}")
    create_database(engine.url)

if __name__ == '__main__':
     # 连接到 MySQL 服务器（不指定数据库）
    engine = create_engine(f"mysql+pymysql://{username}:{password}@127.0.0.1:3306/{database}", echo=True)
    
    # 若有旧的数据库则删除，没有则创建新的数据库
    drop_and_create_database(engine)

    # 重新连接到新创建的数据库
    engine = create_engine(f"mysql+pymysql://{username}:{password}@127.0.0.1:3306/{database}", echo=True)

    # 检查表的存在性，如果不存在的话会执行表的创建工作
    Base.metadata.create_all(bind=engine)

    # 创建缓存对象
    Session = sessionmaker(bind=engine)
    session = Session()
    user_list = [user(username="admin", password="123456", email="admin@zju.edu.cn", role="1"),
                 user(username="zhangsan", password="123456", email="3210105952@zju.edu.cn", role="0"),
                 user(username="lisi", password="123456",
                      email="3210105953@zju.edu.cn", role="0"),
                 user(username="wangwu", password="123456", email="3210105954@zju.edu.cn", role="1"),]
    # session.add(p)
    session.add_all(user_list)
    entry_list = [
        entry(eid=1003, title="卷积神经网络（CNN）是什么？一文读懂卷积神经网络的概念、原理、优缺点和主要应用", category="understanding", content="卷积神经网络（CNN）是一种..."),
        entry(eid=1004, title="情感分析", category="understanding", content="情感分析是自然语言处理的一个分支，它用来..."),
        entry(eid=1005, title="数据标注", category="understanding", content="数据标注是指在数据处理和机器学习中，将原始数据..."),
        entry(eid=1006, title="预训练(Pre-training)", category="home", content="预训练是一种深度学习技术，它涉及到..."),
        entry(eid=1007, title="大语言模型(LLM)", category="understanding", content="大语言模型是一种强大的自然语言处理工具，它..."),
        entry(eid=1008, title="多模态", category="understanding", content="多模态指的是结合了多种类型的数据或输入，如..."),
        entry(eid=1009, title="强化学习(Reinforcement Learning)", category="home", content="强化学习是一种机器学习方法，它基于..."),
        entry(eid=1010, title="无监督学习(Unsupervised Learning)", category="understanding", content="无监督学习是一种机器学习方法，其中..."),
        entry(eid=1011, title="自然语言处理(NLP)", category="understanding", content="自然语言处理是计算机科学和人工智能的一个分支，旨在..."),
        entry(eid=1012, title="通用人工智能(AGI)", category="home", content="通用人工智能是指在任何人类智能任务上都能表现..."),
        entry(eid=1013, title="神经网络(Neural Network)", category="understanding", content="神经网络是一种模仿人脑神经元网络进行信息处理的..."),
    ]
    session.add_all(entry_list)
    entry_submissions = [
        entry_submission(eid=1001, uid = 1, title="什么是LoRA？一文读懂低秩适应的概念、原理、优缺点和主要应用", category="home", content="LoRA（Low-Rank Adaptation）是..."),
        entry_submission(eid=1002, uid = 2, title="什么是RLHF基于人类反馈的强化学习？", category="understanding", content="基于人类反馈的强化学习（RLHF）是..."),
        entry_submission(eid=1014, uid = 3 , title="GAN，生成式对抗网络（Generative Adversarial Network）", category="news", content="生成式对抗网络（GAN）是一种深度学习模型，它由...")
    ]
    session.add_all(entry_submissions)
    error_corrections = [
            error_correction(eid=1004, uid = 1,  title="什么是LoRA？情感分析",  content="LoRA（Low-Rank Adaptation）是..."),
            error_correction(eid=1005, uid = 2,  title="什么数据标注基于人类反馈的强化学习？",  content="基于人类反馈的强化学习（RLHF）是..."),
            error_correction(eid=1013, uid = 3 ,  title="GAN，神经网络（Generative Adversarial Network）",  content="生成式对抗网络（GAN）是一种深度学习模型，它由...")
        ]
    session.add_all(error_corrections)
    user_entry_list= [user_entry(1,1001,"recommend"),
                      user_entry(1,1002,"recommend"),
                      user_entry(1,1003,"like"),
                      user_entry(1,1004,"favor"),
                      user_entry(1,1005,"like"),
                      user_entry(1,1006,"recommend")
                      ]
    session.commit()
