from sqlalchemy import create_engine, Column, Integer, Float, String, DateTime, TIMESTAMP, Double, Text, BLOB
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

from datetime import datetime
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

engine = create_engine(
    "mysql+pymysql://"+username+":"+password+"@127.0.0.1:3306/"+database, echo=True)

# 创建缓存对象
Session = sessionmaker(bind=engine)
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
    content = Column(Text, nullable=False)
    submitTime = Column(DateTime, nullable=False)
    status = Column(DateTime, nullable=False)
    admin = Column(Integer, nullable=False)

    def __repr__(self):
        return self.device_id+'-'+str(self.timestamp)


class entry_submission_review(Base):
    __tablename__ = 'entry_submission_review'
    esrid = Column(Integer, autoincrement=True, primary_key=True)
    admin = Column(Integer, nullable=False)
    esid = Column(Integer, nullable=False)
    status = Column(String(10), nullable=False)
    comment = Column(Text, nullable=False)
    reviewTime = Column(DateTime, nullable=False)


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
    gender = Column(String(5), nullable=True)
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


# 检查表的存在性，如果不存在的话会执行表的创建工作
Base.metadata.create_all(bind=engine)


if __name__ == '__main__':

    session = Session()
    user_list = [user(username="zhangsan", password="123456", email="3210105952@zju.edu.cn", role="0"),
                 user(username="lisi", password="123456",
                      email="3210105953@zju.edu.cn", role="0"),
                 user(username="wangwu", password="123456", email="3210105954@zju.edu.cn", role="1")]
    # session.add(p)
    session.add_all(user_list)
    entry_list = [entry(eid=1001, title="什么是LoRA？一文读懂低秩适应的概念、原理、优缺点和主要应用", category="home"),
                  entry(eid=1002, title="什么是RLHF基于人类反馈的强化学习？",
                        category="understanding"),
                  entry(eid=1003, title="卷积神经网络（CNN）是什么？一文读懂卷积神经网络的概念、原理、优缺点和主要应用",
                        category="understanding"),
                  entry(eid=1004, title="情感分析", category="understanding"),
                  entry(eid=1005, title="数据标注", category="understanding"),
                  entry(eid=1006, title="预训练(Pre-training)", category="home"),
                  entry(eid=1007, title="大语言模型(LLM)",
                        category="understanding"),
                  entry(eid=1008, title="多模态", category="understanding"),
                  entry(eid=1009, title="强化学习(Reinforcement Learning)",
                        category="home"),
                  entry(eid=1010, title="无监督学习(Unsupervised Learning)",
                        category="understanding"),
                  entry(eid=1011, title="自然语言处理(NLP)",
                        category="understanding"),
                  entry(eid=1012, title="通用人工智能(AGI)", category="home"),
                  entry(eid=1013, title="神经网络(Neural Network)",
                        category="understanding"),
                  entry(
                      eid=1014, title="GAN，生成式对抗网络（Generative Adversarial Network）", category="news")
                  ]
    session.add_all(entry_list)
    user_entry_list= [user_entry(1,1001,"recommend"),
                      user_entry(1,1002,"recommend"),
                      user_entry(1,1003,"like"),
                      user_entry(1,1004,"favor"),
                      user_entry(1,1005,"like"),
                      user_entry(1,1006,"recommend")
                      ]
    session.commit()
