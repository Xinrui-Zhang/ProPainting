from bert_sim import BertSim

if __name__ == "__main__":
    bs = BertSim(gpu_no=1, log_dir='log', bert_sim_dir='bert_sim_model\\', verbose=True)
    data = [
            {"id":1,"question":"怎么查看原图的网页啊？","answer":"点击图片详情右下角的参考链接就可以了哦~不过在国内访问维基可能会有困难嗷~"},
            {"id":2,"question":"为什么主页是空白的？","answer":"首先请检查一下网络连接的情况，如果网络没问题的话建议使用chrome浏览器哦~"},
            {"id":3,"question":"你是谁？","answer":"我是由东南大学软件学院的几位18级本科生制作的ProPainting网站的小助手~在使用过程中有任何问题都可以来问我！"},
            {"id":4,"question":"网站都有什么功能？","answer":"在ProPainting，你可以欣赏数万张大师画作和绘画参考图片，同时你还可以使用图像风格迁移、动图生成、色卡生成等功能来让你的绘画更有趣更方便，你还可以点赞、收藏你喜欢的图片，用得越多，我们为你推荐的画作就越符合你的口味哦~更多隐藏功能等你来玩！"},
            {"id":5,"question":"为什么不能上传作品？","answer":"上传的图片不能大于2M，如果超出限制大小了麻烦先压缩一下啦~"},
            {"id":6,"question":"画画好难，不想坚持了","answer":"ProPainting永远是你绘画路上坚实的后盾，加油哦！有时候再多坚持一步就能抵达成功啦！"},
            {"id":7,"question":"图片的版权有问题怎么办","answer":"如果你发现图片的版权有问题，可以点击图片右下方的图片申诉进行举报，如果举报成功，你将获得积分奖励，积分将在后续即将推出的美术课程、志愿咨询等服务中作为代金券哦。"},
            {"id":8,"question":"图片的内容有问题怎么办","answer":"如果你发现图片的内容有问题，可以点击图片右下方的图片申诉进行举报，如果举报成功，你将获得积分奖励，积分将在后续即将推出的美术课程、志愿咨询等服务中作为代金券哦。"},
            {"id":9,"question":"怎么下载","answer":"点击图片左下角的下载图标就可以将画作下载至本地啦，当然，在图片上右键另存为也可以哦。"},
            {"id":10,"question":"生成的色卡我不满意","answer":"首先，很抱歉影响了你的使用体验，对图片生成的色卡不满意的话，可以点击图片右下方的图片申诉进行反馈，我们将及时做出调整。"},
            {"id":11,"question":"图片风格迁移不满意","answer":"首先，很抱歉影响了你的使用体验，对图片风格迁移的效果不满意的话，可以点击图片右下方的图片申诉进行反馈，我们将及时做出调整。"},
            {"id":12,"question":"没有我想要的图片","answer":"很抱歉，我们获取的图片没有满足你的需要，你可以点击图片右下角的图片申诉把你想看的图片告诉我们，我们将尽力通过各种渠道为你找到你所需要的图片，并以站内通知的形式告知你。"},
            {"id":13,"question":"网站有bug","answer":"很抱歉，由于我们的技术能力给你带来了不好的使用体验，请至页脚处链接的github仓库提交issue，我们将尽快做出回应。"},
            {"id":14,"question":"之前收藏的收藏夹无法查看了","answer":"抱歉，很可能是因为图集的创作者将图集设置为私密了"},
            {"id":15,"question":"之前收藏的图片无法查看了","answer":"抱歉，很可能是因为图片的上传者将图片设置为私密了"},
            {"id":16,"question":"我如何知道别人对我的收藏和图片的喜爱程度","answer":"每一次点赞和收藏您都会收到消息，您可以通过点赞和收藏次数来得知 "},
            {"id":17,"question":"生成的动图可以做什么","answer":"通过上传您的图片，生成动图可以让您清楚知道自己的创作过程。如果您制作的是漫画，生成动图可以呈现给您完整的动画"},
            {"id":18,"question":"随机推荐的图片没有我喜爱的","answer":"您可以先注册登录，我们会通过您的点赞和收藏喜爱，给您特殊的智能推荐，让您能够更大可能性浏览到您想要的图片"}
            ]

    while True:
        userQ = input('请输入你的问题：')
        result = 0
        final = 0
        for i in range(len(data)):
            temp = bs.predict([[data[i]['question'],userQ]])
            if temp[0][1] > result:
                result = temp[0][1]
                final = i
        print(data[final]['answer'])




