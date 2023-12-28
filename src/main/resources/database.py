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
    image = Column(String(200), nullable=True)
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
        self.submit_time = submit_time if submit_time else datetime.now() - \
            timedelta(days=random.randint(0, 10))
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
        self.submit_time = submit_time if submit_time else datetime.now() - \
            timedelta(days=random.randint(0, 10))
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
    url = Column(String(256), nullable=False)
    description = Column(String(256), nullable=False)
    category = Column(String(45), nullable=False)
    recommend = Column(Integer, nullable=False)

    def __init__(self, tid, title, href, url, description, category, recommend=1):
        self.tid = tid
        self.title = title
        self.href = href
        self.url = url
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
    engine = create_engine(
        f"mysql+pymysql://{username}:{password}@127.0.0.1:3306/{database}", echo=True)

    # 若有旧的数据库则删除，没有则创建新的数据库
    drop_and_create_database(engine)

    # 重新连接到新创建的数据库
    engine = create_engine(
        f"mysql+pymysql://{username}:{password}@127.0.0.1:3306/{database}", echo=True)

    # 检查表的存在性，如果不存在的话会执行表的创建工作
    Base.metadata.create_all(bind=engine)

    # 创建缓存对象
    Session = sessionmaker(bind=engine)
    session = Session()
    user_list = [user(username="admin", password="123456", email="admin@zju.edu.cn", role="1"),
                 user(username="zhangsan", password="123456",
                      email="3210105952@zju.edu.cn", role="0"),
                 user(username="lisi", password="123456",
                      email="3210105953@zju.edu.cn", role="0"),
                 user(username="wangwu", password="123456", email="3210105954@zju.edu.cn", role="1"),]
    # session.add(p)
    session.add_all(user_list)
    entry_list = [entry(eid=1001,
                        title="LoRA",
                        category="home",
                        image="image/topicPics/what-is-lora.png",
                        content=open("./docs/1.md").read(),
                        description="低秩适应(Low-Rank Adaptation, LoRA)技术允许更快、更有效地将大型语言模型适应特定的任务或领域。本文将概述LoRA是什么、主要组成、工作原理、优点和局限性...",

                        ),
                  entry(eid=1002,
                        title="RLHF",
                        category="understanding",
                        image="image/topicPics/what-is-rlhf.png",
                        content=open("./docs/2.md").read(),
                        description="基于人类反馈的强化学习（RLHF，Reinforcement Learning from Human Feedback）是人工智能（AI）领域的一个新兴研究领域，它将强化学习技术与人类反馈相结合...",

                        ),
                  entry(eid=1003,
                        title="CNN",
                        category="understanding",
                        image="image/topicPics/convolutional-neural-network-1.png",
                        content=open("./docs/3.md").read(),
                        description="卷积神经网络（Convolutional Neural Network，CNN）是一类主要用于计算机视觉领域的深度学习算法，它们在各个领域都有应用，包括图像和视频识别、自然语言处...",

                        ),
                  entry(eid=1004,
                        title="情感分析",
                        category="understanding",
                        image="image/topicPics/what-is-sentiment-analysis-1.png", 
                        content=open("./docs/4.md").read(),
                        description="情感分析是自然语言处理的一个重要方面，它允许组织从非结构化文本数据中提取有价值的见解。通过了解人们的意见和情绪，企业、研究人员和政府可以做出更明智...",

                        ),
                  entry(eid=1005, 
                        title="数据标注",
                        category="understanding",
                        image="image/topicPics/data-annotation.png",
                        content=open("./docs/5.md").read(),
                        description="机器学习过程的一个重要方面便是数据标注（Data Annotation），数据标注是一个对原始数据进行标记和分类的过程，使其可用于训练ML模型。本文将概述数据标注、...",

                          ),
                  entry(eid=1006, 
                        title="预训练", 
                        category="home",
                        image="image/topicPics/what-is-pre-training.png", 
                        content=open("./docs/6.md").read(),
                        description="预训练是现代机器学习模型的支柱，在本篇文章中，我们将探讨预训练的概念定义，它在人工智能中的重要性，用于实现预训练的各种技术，以及该领域的研究人员所...",

                        ),
                  entry(eid=1007,
                         title="大语言模型",
                        category="understanding", 
                        image="image/topicPics/what-is-large-language-model.png", 
                        content=open("./docs/7.md").read(),
                        description="自然语言中最重要的发展便是大语言模型（LLM），在本篇文章中，我们将浅显地科普一下大语言模型，讨论其定义、训练方式、流行原因、常见大语言模型例子以及其...",

                        ),
                  entry(eid=1008, 
                        title="多模态", 
                        category="understanding",
                        image="image/topicPics/what-is-multimodal-deep-learning.png",
                        content=open("./docs/8.md").read(),
                        description="多模态深度学习（英文名：Multimodal Deep Learning）是人工智能（AI）的一个子领域，其重点是开发能够同时处理和学习多种类型数据的模型。本文解释了其定义...",

                          ),
                  entry(eid=1009, 
                        title="强化学习",
                        category="home", 
                        image="image/topicPics/reinforcement-learning.png", 
                        content=open("./docs/9.md").read(),
                        description="强化学习（RL）是机器学习的一个分支，重点是训练算法通过与环境的互动来做出决定。它的灵感来自于人类和动物从他们的经验中学习以实现目标的方式。在这篇文...",

                        ),
                  entry(eid=1010, 
                        title="无监督学习",
                        category="understanding", 
                        image="image/topicPics/what-is-unsupervised-learning-1.png",
                        content=open("./docs/10.md").read(),
                        description="无监督学习是机器学习的一种类型，模型从数据中学习，没有任何明确的指导或标记的例子。本文介绍了其定义、主要算法、应用和挑战。",

                          ),
                  entry(eid=1011, 
                        title="自然语言处理",
                        category="understanding", 
                        image="image/topicPics/what-is-nlp.png",
                        content=open("./docs/11.md").read(),
                        description="NLP（Natural Language Processing），即自然语言处理，是计算机科学的一个领域，重点是创建能够理解人类语音和语言的计算机和软件。NLP使用人工智能和机器学...",

                          ),
                  entry(eid=1012, 
                        title="通用人工智能",
                        category="home",
                        image="image/topicPics/agi-artificial-general-intelligence.png",
                        content=open("./docs/12.md").read(),
                        description="AGI 是 Artificial General Intelligence 的缩写，中文翻译为“通用人工智能”，该术语指的是机器能够完成人类能够完成的任何智力任务的能力。",

                          ),
                  entry(eid=1013, 
                        title="神经网络",
                        category="understanding", 
                        image="image/topicPics/what-is-neural-network.png",
                        content=open("./docs/13.md").read(),
                        description="神经网络（Neural Network）作为人工智能中的一种计算模型，是受人脑启发的一种机器学习类型。本文介绍了其定义、工作原理、类型、优势、局限和其应用场景。",

                          ),
                  entry(
                      eid=1014, 
                      title="生成式对抗网络", 
                      category="news", 
                      image="image/topicPics/what-is-gan.png", 
                      content=open("./docs/14.md").read(),
                      description="生成式对抗网络（GAN，英文全称Generative Adversarial Network）是一种深度学习模型，由于其生成高质量、真实数据的能力，近年来获得了极大的关注。在这篇文...",

                      ),
                  entry(
                      eid=1015,
                        title="AIGC", 
                        category="news", 
                        image="image/topicPics/what-is-aigc.png",
                        content=open("./docs/14.md").read(),
                        description="本文介绍了什么是AIGC：AI Generated Content，人工智能生成内容及其工作原理、应用场景和面临的挑战",

                          ),
                  entry(
                      eid=1016, 
                      title="深度学习", 
                      category="news", 
                      image="image/topicPics/what-is-deep-learning.png", 
                      content=open("./docs/14.md").read(),
                      description="深度学习（Deep Learning）是机器学习的一个子集，本文介绍了什么是深度学习、深度学习的工作原理、深度学习与机器学习的区别、深度学习的开发框架以及深度学...",

                      ),
                  entry(
                      eid=1017, 
                      title="机器学习", 
                      category="news", 
                      image="image/topicPics/what-is-machine-learning.png", 
                      content=open("./docs/14.md").read(),
                      description="本文介绍了什么是机器学习、机器学习的技术分类、机器学习的常见算法以及机器学习的实际应用，让你对机器学习这一AI技术有一个初步的认识。",

                      ),
                  #   entry(
                  #       eid=1014, title="GAN，生成式对抗网络（Generative Adversarial Network）", category="news", image="image/topicPics/what-is-gan.png", content=open("./docs/14.md").read()),
                  #   entry(
                  #       eid=1014, title="GAN，生成式对抗网络（Generative Adversarial Network）", category="news", image="image/topicPics/what-is-gan.png", content=open("./docs/14.md").read()),

                  ]
    session.add_all(entry_list)
    entry_submissions = [
        entry_submission(eid=1001, uid=1, title="什么是LoRA？一文读懂低秩适应的概念、原理、优缺点和主要应用",
                         category="home", content="LoRA（Low-Rank Adaptation）是..."),
        entry_submission(eid=1002, uid=2, title="什么是RLHF基于人类反馈的强化学习？",
                         category="understanding", content="基于人类反馈的强化学习（RLHF）是..."),
        entry_submission(eid=1014, uid=3, title="GAN，生成式对抗网络（Generative Adversarial Network）",
                         category="news", content="生成式对抗网络（GAN）是一种深度学习模型，它由...")
    ]
    session.add_all(entry_submissions)
    error_corrections = [
        error_correction(eid=1004, uid=1,  title="什么是LoRA？情感分析",
                         content="LoRA（Low-Rank Adaptation）是..."),
        error_correction(eid=1005, uid=2,  title="什么数据标注基于人类反馈的强化学习？",
                         content="基于人类反馈的强化学习（RLHF）是..."),
        error_correction(eid=1013, uid=3,  title="GAN，神经网络（Generative Adversarial Network）",
                         content="生成式对抗网络（GAN）是一种深度学习模型，它由...")
    ]
    session.add_all(error_corrections)
    user_entry_list = [user_entry(1, 1001, "recommend"),
                       user_entry(1, 1002, "recommend"),
                       user_entry(1, 1003, "like"),
                       user_entry(1, 1004, "favor"),
                       user_entry(1, 1005, "like"),
                       user_entry(1, 1006, "recommend")
                       ]
    tool_array = eval(open('/home/azx/Desktop/AIWiki/src/main/resources/toolText.py').read())
    tool_list = []
    for map in tool_array:
        tool_list.append(tool(**map))
    session.add_all(tool_list)
    session.commit()
