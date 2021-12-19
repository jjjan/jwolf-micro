<template>
  <div class="dashboard-container">
    <div class="dashboard-text">name: {{ name }}</div>
    <el-button @click="me">ME</el-button>
     <div>
		用户ID:<input v-model="userId">
		<button @click="connect">登录聊天室</button>
		<button @click="leave">退出聊天室</button>
		<button @click="offline">下线</button>
		发送给：
		<input v-model="message.toUser">
		消息：<input v-model="message.message">
		<button @click="send">发送</button>
		接受到消息：
		<div v-html="info">
		</div>
	</div>
  </div>

</template>

<script>
import { mapGetters } from 'vuex'
import {getInfo} from '@/api/user'
import SockJS from "sockjs-client"
	import Stomp from "stompjs"
export default {
  name: 'Dashboard',
  data(){
    return {
      userId: "",
				stompClient: null,
				message: {
					toUser: '',
					fromUser:'',
					message: ''
				},
				info: ""
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  mounted() {
  // getInfo()
  },
  activated() {

    getInfo()
  },
  methods:{
    me(){
      getInfo().then(resp=>{
       // if(resp.headers['content-type']=="text/html" && resp.request.responseURL){
	  //window.location.href = resp.request.responseURL;
	//}


      })
    },
    connect() {
				let socket = new SockJS('http://localhost:8881/ws/jwolf')
				this.stompClient = Stomp.over(socket)
				this.stompClient.connect({
					"userId": this.userId
				}, this.connectSuccess, this.connectError);
			},
			//注意/user前缀见后端配置类
			connectSuccess(obj) {
				this.stompClient.subscribe('/topic/AAA', (msg) => {
					if (msg.body.startsWith("系统消息AAA")) {
						this.info += "<br>" + msg.body;
					} else {
						let body = JSON.parse(msg.body)
						this.info += "<br>==>" + body.date + "  "+body.fromUser+"AAA说:" + body.message
					}


				});
        this.stompClient.subscribe('/topic/BBB', (msg) => {
					if (msg.body.startsWith("系统消息BBB")) {
						this.info += "<br>" + msg.body;
					} else {
						let body = JSON.parse(msg.body)
						this.info += "<br>==>" + body.date + "  "+body.fromUser+"BBB说:" + body.message
					}


				});
			},
			connectError(err) {
				console.log("网络异常")
			},
            //注意/app前缀，/app/send映射到后端chatController
			send() {
				this.message.fromUser=this.userId;
				this.info += "<br>                               <== "+this.message.message;
				this.stompClient.send("/user/user/page", {}, JSON.stringify(this.message));
			},
			offline() {
				this.stompClient.disconnect();
				this.info += "<br>成功退出系统";
			},
			//注意这里不用前缀
			leave() {
				this.stompClient.unsubscribe('/topic/AAA')
			}



  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
