# AI绘画发展史

图片生成是元象元宇宙能力的重要组成部分，一方面它参与构建了3D世界，另一方面也极大提升了内容生产力，我们将密切关注其发展与应用潜能。那我们就聚焦**生成式大模型（Generative Models）与图片应用，用一篇文章捋清楚它的发展脉络。**

先奉上一张图，根据时间线，我们梳理出常见AIGC图片生成（AI绘画）的深度学习模型。

![img](https://picx.zhimg.com/80/v2-169e3d5a6814b36e2554897f856ac380_720w.webp?source=1def8aca)

图像生成式大模型（Generative Models）发展

全文6000字，预计阅读20分钟。

## **开天辟地**

2022年8月，游戏设计师Jason Allen凭借AI绘画作品《太空歌剧院（Théâtre D’opéra Spatial）》获得美国科罗拉多州博览会“数字艺术/数码摄影“竞赛单元一等奖，“AI绘画”引发全球热议。而此前DALL-E、Stable Diffusion、Midjourney等AI创作工具雨后春笋般出现，催生出了概念词“AIGC”、大众词“AI 绘画“，再到技术词“txt2img“（文生图）。不过究其根本，都绕不开人工智能领域重要成果——生成式模型（Generative Model）。

生成式模型是概率统计和机器学习中的一类重要模型，它根据给定数据集估计整个数据分布，并利用该估计分布采样，生成数据点。

![img](https://pic1.zhimg.com/80/v2-a070f7455029e4673d547ab01d3d45cd_720w.webp?source=1def8aca)

生成模型可以根据观测数据对分布进行估计

大家熟知的[朴素贝叶斯](https://www.zhihu.com/search?q=朴素贝叶斯&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})（Naive Bayes）、[高斯混合模型](https://www.zhihu.com/search?q=高斯混合模型&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})（Gaussian Mixture Model, GMM)、隐马尔科夫模型（Hidden Markov Model , HMM）等，都是经典生成模型，但之前受困于高维数据缺乏和过于严格的假设，只能用在简单的预测和聚类任务。随着深度学习、硬件算力和大规模数据集发展，生成模型也迎来了技术革新。



![img](https://pic1.zhimg.com/80/v2-dad04d48a156bd8afad5def0d231c176_720w.webp?source=1def8aca)

生成模型的分类

### VAE和GAN的出现

2012年开启的深度学习黄金时代，研究者最关注变分自动编码器（Variational Autoencoder, VAE）和[生成对抗网络](https://www.zhihu.com/search?q=生成对抗网络&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})（Generative Adversial Network, GAN)。

VAE模型基于阿姆斯特丹大学发表的《Auto-Encoding Variational Bayes》 论文，在训练过程中，编码器会把所有数据压到一个可定义的先验分布中，实际应用中常使用标准正态分布，在分布中随机采样，解码器就能生成随机数据样本。但过强的先验假设限制了模型拟合能力，使其不能模拟复杂分布，生成图像往往非常模糊。

![img](https://pica.zhimg.com/80/v2-4195577c54d70911d2d7bc78128f0a0d_720w.webp?source=1def8aca)

VAE生成的图片

VAE出现同年，谷歌科学家Ian Goodfellow发表《Generative Adversarial Networks》，GAN从此诞生。两个网络，一个生成器从先验分布中生成图像，另一个判别器判断生成图像的“真假”。生成器为了“骗过”判别器，尽量生成看起来真实的图像，判别器通过这些越来越真的样例，提升辨别能力，最终两者达到平衡，让生成器生成符合真实数据分布的样本。

这一天才的想法据说来自Goodfellow喝酒时的”异想天开“，这灵光一现也点燃了些许沉闷的图像生成领域。

![img](https://pic1.zhimg.com/80/v2-4a12cef4a998ca45476370c60918c5b6_720w.webp?source=1def8aca)

GAN生成的图片

如果按有无条件引导，生成模型可分为无条件生成和有条件生成，前者用来探索模型效果上限，生成的图片取决于训练数据，内容较单一；后者更晚出现，可利用类别、文本描述、图片等输入信息引导图片生成，训练数据也更为丰富。

VAE和GAN代表切实有效的图像生成器诞生，学界也不断提升其能力。2015年条件生成模型CVAE和CGAN发表，通过类别标签等引导条件，可生成指定类别图片，实现了对AI生成内容的控制。而同一年基于深度递归注意力写入器DRAW（Deep Recurrent Attentive Writer，DRAW）提出的AlignDRAW模型，则将类别标签替换为文本描述，生成36*36分辨率的图像，文本生成图像（text-to-image）任务首次实现。

![img](https://picx.zhimg.com/80/v2-11f66b5501ddf45d5d63f656fbcb945a_720w.webp?source=1def8aca)

AlignDRAW生成的图片

然而基于VAE的AlignDRAW生成的图像较为模糊，分辨率也较低，即使研究人员尝试用GAN模型锐化图像边缘也并没有取得太好的效果。

### 首个基于GAN的txt2img模型

2016年，第一个基于GAN的txt2img模型GAN-INT-CLS诞生，分辨率64*64，证明了GANs能从文本生成较好图像，为学界打开了新世界的大门，各类基于GAN的有条件图像生成模型大量涌现。



![img](https://picx.zhimg.com/80/v2-82d44aa3c54067cc4475aad6396fac04_720w.webp?source=1def8aca)

GAN-INT-CLS生成的图片

此时的模型面临众多问题：无法捕获对象描述中的定位约束、生成图分辨率不够高、文本描述细节和图像保真度不尽如意等。因此发展出了条件生成模型的两种常见思路：条件编码或图像生成，前者要尽量详尽地保留输入意图并易于生成器理解，后者则需尽可能理解条件编码并生成对应高质量图像。

学界同年继续跟进，相继提出GAWWN模型（Learning What and Where to Draw），从条件编码入手，支持输入具体位置描述来绘制对应区域内容，分辨率也提高到128*128；提出StackGAN和StackGAN++模型，从优化生成器出发，另辟蹊径提出堆叠思想，将复杂问题分解为多个子问题，将多个GAN像栈一样串行，第一个模型生成包含对象的粗糙形状和颜色的低分辨率图像，后续GAN模型在前序模型上以提升分辨率为目标训练，分布实现高分辨率文生图任务（分辨率256*256）；基于前者引入[attention机制](https://www.zhihu.com/search?q=attention机制&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})的AttnGAN，将条件编码与图像生成以attention方式关联，进一步深入用户输入理解，克服图像细节丢失问题（分辨率256*256）。

![img](https://picx.zhimg.com/80/v2-fccba4130c82da90c437f6cebe24ee4a_720w.webp?source=1def8aca)

StackGAN与GAN-INT-CLS生成图片对比

![img](https://pic1.zhimg.com/80/v2-987516e45d390619283bd7d5c73b6472_720w.webp?source=1def8aca)

AttnGAN生成的图片

GAN还有个棘手问题：[模式崩溃](https://www.zhihu.com/search?q=模式崩溃&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})（Mode Collapse)，即模型总生成重复图像，缺乏多样性。WGAN细致分析其原因，提出了新的[损失函数](https://www.zhihu.com/search?q=损失函数&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})来解决。到2017年，基于GAN提出的Pixel-to-Pixel和CycleGAN实现了风格迁移和纹理修改等多种图生图应用，极大提升AI生成实用性。

![img](https://pic1.zhimg.com/80/v2-269f123a2d45868fcb5a410f53dd8e76_720w.webp?source=1def8aca)

Pix2Pix

![img](https://picx.zhimg.com/80/v2-fa801ca3707a0325b8bb1da615488a04_720w.webp?source=1def8aca)

CycleGAN



### ProgressiveGAN与StyleGAN

同年10月，NVIDIA提出ProgressiveGAN，从小分辨率图像生成，逐渐增加神经网络规模，生成了质量空前的高清图像，通过减小模型训练难度并提高生成质量，为之后大放异彩的StyleGAN铺平道路。

![img](https://pic1.zhimg.com/80/v2-366172943de11ff7ced26d8fe522e56e_720w.webp?source=1def8aca)

ProgressiveGAN生成的图像

StyleGAN系列的核心是风格调制（Style Modulation），网络先将先验噪声映射到一个新的隐空间中，被称为W空间，映射后的隐变量输入到生成器的多层次中，通过规范化层注入到生成过程，使模型在生成高质量图像基础上做到层次特征可控。如生成人脸时，低层次控制是不同五官或人脸特征生成，高层次特征决定生成颜色，因可控性常用于风格迁移或图像编辑。

![img](https://picx.zhimg.com/80/v2-15b209621d4bbca1ebc7c26f1d6fbed4_720w.webp?source=1def8aca)

使用StyleGAN进行图像生成或编辑



## **举步维艰**

2014年GAN被提出后发展如火如荼，但是训练不稳定、生成图像常出现伪影（Artifacts）、只在质量较高且类型单一的数据集上表现出色等问题，让新应用举步维艰。

### Transformer结构的提出

当科技陷入黎明前的黑暗时，智慧火花迸发。2017年谷歌发表著名论文《[Attention Is All You Need](https://www.zhihu.com/search?q=Attention Is All You Need&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})》，提出Transformer结构，随即席卷各类自然语言处理任务；2020年又提出ViT（Vision Transformer）概念，尝试用Transformer结构代替传统计算机视觉中的[卷积神经网络](https://www.zhihu.com/search?q=卷积神经网络&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})（Convolutional Neural Network，CNN）结构。



![img](https://picx.zhimg.com/80/v2-efec4b44a0278ba86924ad5862edbdbf_720w.webp?source=1def8aca)

ViT结构示意图

一夜之间，条件生成模型的条件编码与图像生成模块有了新的屠龙工具，VitGAN、ViT-VQGAN等换装模型随即诞生。

### OpenAI发布DALL-E与CLIP

2021年1月，OpenAI发布基于VQVAE模型的DALL-E（未开源）和CLIP模型（Contrastive Language-Image Pre-Training，已开源），前者用于text-to-image任务，后者用文本作监督信号训练可迁移视觉模型，AI好像第一次真正“听懂”人类描述再进行创作，从而激发了人类空前高涨的“AI绘画”热情。

![img](https://pic1.zhimg.com/80/v2-7ef66842be472f836c7b0cefa791fb63_720w.webp?source=1def8aca)

DALL-E生成的牛油果椅子

![img](https://pic1.zhimg.com/80/v2-9d0b9fe18f78b15ce1c3e924148c9af9_720w.webp?source=1def8aca)

CLIP结构示意图

CLIP的能力不止于此。作为比对文本-图像对的预训练模型，它能将文本和图像转化为特征向量，计算出任意一对文本-图像的匹配度。如果将该匹配验证过程链接到AI图像生成模型之上，依靠CLIP的匹配验证，引导图像生成器推导出的图像特征值向量匹配指定的文本条件编码向量，不就能得到符合文字描述的图片吗？

与CLIP链接的模型因此纷至沓来，出现了CLIP+StyleGAN、CLIP+VQVAE、到风靡整个开源社区的CLIP+VQGAN，有条件图像生成借助CLIP再次出圈，基本上解决了文本-图片的多模态问题，但由于GAN类模型生成结果仍不尽如人意，AIGC仍无法进入大众视野，而属于Diffusion Model的时代即将到来！

![img](https://picx.zhimg.com/80/v2-e2a8cfdfb4c90b7c27622d566b700c9c_720w.webp?source=1def8aca)

VQGAN-CLIP结构简图

![img](https://picx.zhimg.com/80/v2-18792caf48bc8f2e64ae5d72f5c7005c_720w.webp?source=1def8aca)

VQGAN-CLIP生成的图片

## **柳暗花明**

### Diffusion Models

Diffusion就是“去噪点”，类似手机拍夜景时自动降噪功能。如果反复去噪点计算，极端情况下，能把满是噪点的图片还原清晰吗？AI边猜边去躁似乎可行，这就是扩散模型的由来。

斯坦福大学2015年将非均衡热力学引入深度无监督学习中，为当今大火的扩散模型（Diffusion Models）奠定基础。此模型定义了一个基于[马尔科夫链](https://www.zhihu.com/search?q=马尔科夫链&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})的扩散过程，逐渐在数据中加入随机噪声，再学习扩散的逆过程，从一个随机噪声开始，还原数据原貌。扩散模型有着良好的数学性质，假如每次叠加的噪声符合正态分布，那么无穷多步叠加后，得到的结果也符合正态分布，那么就可以从这样一个分布中采样噪声，恢复出数据。但模型原理决定了它无法像VAE和GAN高效地一步生成数据，因此未引起足够重视。

2020年转机出现。[加州大学伯克利分校](https://www.zhihu.com/search?q=加州大学伯克利分校&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})提出了大家熟知的去噪扩散概率模型（Denoising Diffusion Probabilistic Models，DDPM），简化了原有模型的损失函数，将训练目标转为预测当前步添加的噪声信息，极大降低了训练难度，并将网络模块由[全卷积网络](https://www.zhihu.com/search?q=全卷积网络&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})替换为Unet，提升模型表达能力。



![img](https://pic1.zhimg.com/80/v2-2a51cdc02036cfcaddb8afb7fd5abe7c_720w.webp?source=1def8aca)

去噪扩散模型

![img](https://picx.zhimg.com/80/v2-33c613f056ab21ee1c64060aad06a9cf_720w.webp?source=1def8aca)

DDPM生成的图像

DDPM终于赶上了3年前ProgressiveGAN的生成效果，但需上千步迭代。DDPM用一张[英伟达2080TI显卡](https://www.zhihu.com/search?q=英伟达2080TI显卡&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})生成5万张图片（分辨率32*32）需20小时，而GAN只需不到1分钟，生成低效是制约DDPM发展的最大障碍。

2021年的Improved DDPM和DDIM（Denoising Diffusion Implicit Models）都进行了优化和加速。DDIM只采样原本扩散步的一个子集，在采样多样性和采样效率上取舍，在小于100扩散步下生成了高质量图像。

### 清华大学提出DPM-Solver

2022年，清华大学提出的DPM-Solver从[常微分方程](https://www.zhihu.com/search?q=常微分方程&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})（ODE）角度进一步提升模型采样速度，无需重新训练，在10-25步内得到高质量样本，并于同年提出该采样算法的改进版本DPM-Solver++，进一步提升采样稳定性。在计算资源较少时，采用这类高阶ODE Solver是一个不错选择。随着采样器改进和不断修正，扩散模型真正从可用变好用，生成质量越来越高，生成一张图的时间也从数分钟减少到数秒。



![img](https://picx.zhimg.com/80/v2-313cdec6e54db4177b19af4fd38ccacf_720w.webp?source=1def8aca)

Semantic Diffusion Guidance(SDG)的classifer guidance原理图

![img](https://picx.zhimg.com/80/v2-e5c228002fb2194001eafac95f77c732_720w.webp?source=1def8aca)

Stable Diffusion的classifer-free guidance原理图

在条件生成方面，扩散模型也分为两类，classifer guidance和classifer-free guidance，前者在已经训练好的扩散模型上再额外训练一个分类模型来判断模型采样生成的图片类别，然后反馈给扩散模型，通过降低模型多样性来引导模型生成指定内容图片，这也导致模型生成图片效果往往取决于额外训练模型的质量；后者则直接把引导条件也作为扩散模型输入的一部分，参与到整个模型训练中，最终让扩散模型获得根据引导条件就直接生成指定内容图片的能力。

## **百花齐放**

### **谷歌发布**Disco Diffusion

2021年10月，谷歌发布的Disco Diffusion模型以其惊人的图像生成效果拉开了属于扩散模型的AIGC时代序幕。

![img](https://pic1.zhimg.com/80/v2-e7863c77b6c7da3cd15ede3fe2591fc9_720w.webp?source=1def8aca)

Disco diffusion生成的图片——“A beautiful painting of a singular lighthouse, shining its light across a tumultuous sea of blood by greg rutkowski and thomas kinkade, Trending on artstation.”

但它依然存在两大问题：图像精美但细节不够深入；生成耗时过长（也是一直以来的痛点）。但其图像质量碾压以往众多绘画模型，达到普通人无法企及高度，这才让AI绘画真正迈入生产工具行列。

扩散模型于2022年得到扩散式发展，广泛应用到文生图、图像翻译、超分辨率、甚至是视频或3D模型生成领域，让AIGC概念火热发展，大厂角逐，OpenAI的DALL·E2、谷歌的Imagen和百度的文心一格相继推出。

![img](https://pic1.zhimg.com/80/v2-dbba13565b1f987af311c26085760d13_720w.webp?source=1def8aca)

文生图

大数据+大模型赋予了扩散模型强大能力，DALL·E2模型训练使用6.5亿图像数据集，模型规模有10到100亿参数量级的不同变体。而数据和算力则提升了游戏门槛，DALL·E2公测后收费，谷歌Imagen无公测、阿里和百度等AIGC大模型开始包月或按次收费。

### Midjourney与Stable diffusion

打破大厂垄断的，首先是2022年7月公测的Midjourney。作为一个精调生成模型，以聊天机器人方式部署在Discord，目前有980万用户，而开头提到的《[太空歌剧院](https://www.zhihu.com/search?q=太空歌剧院&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2929985388})》即出自于它。

Stable diffusion则真正改变了游戏规则，其背后的美国初创公司Stability.AI为其提供大量算力，并于2022年8月完全开源其算法和预训练模型。它基于Latent Diffusion Models，将最耗时的扩散过程放在低纬度隐变量空间，大大降低算力需求以及个人部署门槛。

比如它使用的潜空间编码缩减因子为8，换句话说就是图像长和宽缩减为八分之一， 一个512*512的图像在潜空间中直接变为64*64, 节省了8*8=64倍内存！它又快又好，就是能快速（以秒计算）生成一张饱含细节的512*512图像, 只需一张英伟达消费级8GB 2060显卡。

而如果没有这个空间压缩转换，它将需要一张8Gx64=512G显存的超级显卡。按显卡硬件发展规律，消费者至少8-10年后才能享受此类应用。一个算法上的重要迭代，就让AI作画提前来到了每个人面前。

开源社区HuggingFace很快对其适配，让个人部署简单化；开源工具Stable-diffusion-webui则将多种图像生成工具集于一体，甚至可在网络端微调模型，训练个人专属模型，广受好评，在Github获得3.4万星，让扩散生成模型彻底出圈，从大型服务走向个人部署。

![img](https://picx.zhimg.com/80/v2-055629a1bce9a625966f2db4ce89931c_720w.webp?source=1def8aca)

Stable diffusion生成的图片

好用的开源工具激发了用户的创作热情，纷纷在社区分享自己的微调模型。二次元绘画模型NovelAI展现了强大的风格绘画能力，许多专业画师使用AI工具辅助工作，更多设计师使用AI获取灵感，商家也开始定制专用模型，比如Prisma相片编辑器功能、抖音的图像风格化玩法，这一次，扩散模型“走入寻常百姓家”，从好用到人人爱用。

## **伦理道德风险**

AIGC发展的如此之快是许多人始料未及的，相关的行业规则无法得到及时更新，可能造成许多伦理问题，比如许多画师认为AI不会创新，使用AI绘画是一种“剽窃”行为，它模仿了一些画师的风格，应该对其进行抵制。人类学习绘画也是从模仿开始，真正能够创新的人少之又少，虽然AI无法做到创新，但从已有的数据中做到融合也极具价值，它可以替代从业者完成低级的劳动，将主要精力放在创新性的工作上来。“剽窃”说并不是AIGC发展的阻力，真正应该担心是隐私泄露问题，许多模型在真人数据集上进行训练，并且个人模型会使用大量用户自己收集的一些图片，这都会在生成的图像中有所体现。如何规范化数据以及生成模型的使用，更好的保护知识产权和个人隐私是行业从业者需要关注且亟需解决的问题。

同时，随着AIGC生成能力越来越强大，文本引导生成或者图片修改几乎达到了以假乱真的地步，这些虚假图片可能蒙蔽、欺骗那些不了解这项技术或者没有注意到其中风险的人们，给他们带来潜在的损失；当前完全开源的AIGC模型，也带来了生成不恰当的图片内容（暴力、色情、歧视）的风险，这也是从业者乃至使用者需要注意的问题。