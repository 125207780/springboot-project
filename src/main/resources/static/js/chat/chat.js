$(document).ready(function() {
    // 初始化用户列表
    initUserList();

    let urlPrefix = 'ws://localhost:8080/net/websocket/';
    let username = $('#user_name').text();
    let ws = initMsg(urlPrefix, username);

    // 客户端发送对某一个客户的消息到服务器
    $('#send').click(function() {
        let selectUserInfo = null;
        // 获取左侧聊天对象
        $(".select_user").each(function() {
            if ($(this).attr("data") === "selected") {
                selectUserInfo = $(this).html();
            }
        });
        if (!selectUserInfo) {
            $("#error_select_msg").html("请选择一个用户！");
            return;
        }

        // 获取图片对象
        let fileList = document.getElementById("file").files;
        let msg_content;
        // 发送图片信息
        if(fileList.length > 0) {
            let fileReader = new FileReader();
            fileReader.readAsDataURL(fileList[0]);
            fileReader.onload = function (e) {
                let msg_content1 = {
                    msg: e.target.result,       // 消息内容
                    acceptUser: selectUserInfo, // 接收消息方
                    sendUser: username,         // 发送消息方
                    sendType: "0",              // 发送类型：0私聊；1群聊
                    msgType: 0                  // 消息类型：0文本；1图片
                };
                if (ws) {
                    ws.send(JSON.stringify(msg_content1));
                    //服务端发送的消息
                    $('#message_chat').append('<div style="width: 100%; float: right;"><span style="float: right;">' + username + '&nbsp;&nbsp;</span><br/>');
                    $('#message_chat').append('<span style="float: right; font-size: 18px; font-weight: bolder;"><img style="width: 150px; height: auto;" src="' + e.target.result + '" /></span></div>');
                    $("#chat_msg").val('');
                    $("#error_select_msg").empty();
                    // 让滚动条跟随消息一直在最底部
                    $('#message_chat').scrollTop($('#message_chat')[0].scrollHeight);
                    $("#chat_msg").focus();
                }
            }
        }
        // 发送文本消息
        else {
            let msg = $("#chat_msg").val();
            if (!msg) {
                alert("请输入聊天内容！");
                return;
            }
            // msg = msg + "[" + selectUserInfo + "]" + "----------" + username;
            msg_content = {
                msg: msg,                   // 消息内容
                acceptUser: selectUserInfo, // 接收消息方
                sendUser: username,         // 发送消息方
                sendType: "0",              // 发送类型：0私聊；1群聊
                msgType: 0                  // 消息类型：0文本；1图片
            };
            if (ws) {
                ws.send(JSON.stringify(msg_content));
                //服务端发送的消息
                $('#message_chat').append('<div style="width: 100%; float: right;"><span style="float: right;">' + username + '&nbsp;&nbsp;</span><br/>');
                $('#message_chat').append('<span style="float: right; font-size: 18px; font-weight: bolder;">' + msg + '</span></div>');
                $("#chat_msg").val('');
                $("#error_select_msg").empty();
                // 让滚动条跟随消息一直在最底部
                $('#message_chat').scrollTop($('#message_chat')[0].scrollHeight);
                $("#chat_msg").focus();
            }
        }
    });

    // 客户端群发消息到服务器
    $('#send_all').click(function() {
        let msg = $('#chat_msg').val();
        if (!msg) {
            alert("请输入聊天内容！");
            return;
        }
        // msg = msg + "[allUsers]" + "----------" + username;
        let msg_content = {
            msg: msg,           // 消息内容
            acceptUser: null,   // 接收消息方
            sendUser: username, // 发送消息方
            sendType: "1",      // 发送类型：0私聊；1群聊
            msgType: "0"        // 消息类型：0文本；1图片
        }
        if (ws) {
            ws.send(JSON.stringify(msg_content));
            //服务端发送的消息
            $('#message_chat').append('<div style="width: 100%; float: right;"><span style="float: right;">' + username + ' 的群发消息&nbsp;&nbsp;</span><br/>');
            $('#message_chat').append('<span style="float: right; font-size: 18px; font-weight: bolder;">' + msg + '</span></div>');
            $("#chat_msg").val('');
            $("#error_select_msg").empty();
            $('#message_chat').scrollTop($('#message_chat')[0].scrollHeight);
            $("#chat_msg").focus();
        }
    });

    // 退出聊天室
    $('#user_exit').click(function() {
        if (ws) {
            $('#message_chat').append('<div style="width: 100%; float: left;">用户[' + username + '] 已经离开聊天室!' + '</div>');
            console.log("用户：[" + username + "]已关闭 websocket 连接...");
            ws.close();
        }
        window.location.href = "/chat/login";
    });
});

//发送图片消息
function chooseFile() {
    let fileList = document.getElementById("file").files;
    let type = fileList[0].type;
    // let selectUserInfo = null;
    // 获取左侧聊天对象
    // $(".select_user").each(function() {
    //     if ($(this).attr("data") === "selected") {
    //         selectUserInfo = $(this).html();
    //     }
    // });
    // let toUser = selectUserInfo;
    let fileMsg;
    if(fileList.length > 0) {
        let fileReader = new FileReader();
        fileReader.readAsDataURL(fileList[0]);
        fileReader.onload = function (e) {
            debugger;
            fileMsg = e.target.result;
            // let username = $('#user_name').text();
            // let msg_content = {
            //     msg: e.target.result,
            //     acceptUser: toUser,
            //     sendUser: username,
            //     sendType: "1",
            //     msgType: "1"
            // };
            // ws.send(JSON.stringify(msg_content));
            return fileMsg;
        }
    } else {
        return null;
    }
}

/**
 * 初始化用户列表
 */
function initUserList() {
    let username = $('#user_name').text();
    $.ajax({
        url: "/getUserList",
        type: "POST",
        data: {username: username},
        success: function(data) {
            let result = JSON.parse(data);
            let html = "<option value=''>---请选择---</option>";
            for (let i = 0; i < result.length; i++) {
                html += "<option value='" + result[i].username + "'>" + result[i].username + "</option>";
            }
            let userList = "";
            for (let i = 0; i < result.length; i++) {
                if (i === 0) {
                    userList += "<div class='select_user' style='background-color: #adb5bd;' data='selected'>" + result[i].username + "</div>";
                } else {
                    userList += "<div class='select_user'>" + result[i].username + "</div>";
                }
            }
            $("#user_list").html(html);
            $("#message_user_count").text(result.length + "人");
            $("#message_user").append(userList);
            $("#message_user").find("div").on("click", function() {
                $(".select_user").each(function() {
                    $(this).removeAttr("style");
                    $(this).removeAttr("data");
                });
                $(this).css("background-color", "#adb5bd");
                $(this).attr("data", "selected");
            });
        }
    });
}

/**
 * 初始化消息
 *
 * @param urlPrefix
 * @param username
 * @returns {WebSocket}
 */
function initMsg(urlPrefix, username) {
    let url = urlPrefix + username;
    let ws = new WebSocket(url);
    ws.onopen = function () {
        console.log("建立 websocket 连接...");
    };
    ws.onmessage = function(event) {
        // 服务端发送的消息
        $('#message_chat').append(event.data + '\n');
    };
    ws.onclose = function() {
        $('#message_chat').append('<div style="width: 100%; float: left;">用户[' + username + '] 已经离开聊天室!' + '</div>');
        console.log("用户：[" + username + "]已关闭 websocket 连接...");
    }
    return ws;
}
