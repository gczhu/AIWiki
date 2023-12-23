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

user = "root"
password = "azx"
database = "ai_wiki"

engine = create_engine(
    "mysql+pymysql://"+user+":"+password+"@127.0.0.1:3306/"+database, echo=True)

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
    question = Column(Text, nullable=False, unique=True)
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
    image = Column(BLOB)
    visits = Column(Integer, default=0)
    likes = Column(Integer, default=0)
    favors = Column(Integer, default=0)

    def __repr__(self):
        return self.eid


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


class user(Base):
    __tablename__ = 'user'
    uid = Column(Integer, autoincrement=True, primary_key=True)
    username = Column(String(10), nullable=False)
    password = Column(String(20), nullable=False)
    email = Column(String(30), nullable=False)
    role = Column(Integer, nullable=False)
    points = Column(Integer, nullable=False)
    level = Column(Integer, nullable=False)
    name = Column(String(30), nullable=True)
    gender = Column(String(5), nullable=True)
    phone = Column(String(20), nullable=True)
    birth = Column(DateTime, nullable=True)
    image = Column(String(256), nullable=True)
    description = Column(String(200), nullable=True)


class user_entry(Base):
    __tablename__ = 'user_entry'
    ueid = Column(Integer, autoincrement=True, primary_key=True)
    uid = Column(Integer, nullable=False)
    eid = Column(String(10), nullable=False)
    type = Column(String(15), nullable=False)


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
    # session.add(p)
    # session.add_all((p1, p2))
    session.commit()

