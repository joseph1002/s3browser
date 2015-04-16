/**
 * Created by Joseph on 4/13/2015.
 */
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

var activeBucket = "";

function connectServer() {
    $.ajax({
        cache: false,
        type: "POST",
        url: "/cos/entry",
        data:{
            "host": $("#host").val(),
            "accessKey": $("#accessKey").val(),
            "secretKey": $("#secretKey").val(),
            "protocol": $("#protocol").val()
        },
//                data:$('#connectForm').serialize(),
        async: false,
        error: function(request) {
            alert("Connection error:" + request);
        },
        success: function(data) {
            console.log(data);
            // obj.hasOwnProperty("key")
            if ("accountOwner" in data) {
                var obj = data["accountOwner"];
                console.log(obj)
            }
            getBuckets();
        }
    });
}

function getBuckets() {
    $.ajax({
        cache: false,
        type: "GET",
        url: "/cos/buckets",
        async: false,
        error: function (request) {
            alert("Get bucket error:" + request);
        },
        success: function (data) {
            var html = "";
            for (var i in data) {
                if (i == 0) {
                    activeBucket = data[i]["name"];
                }
                var bucket = data[i]["name"];
                var liId = 'li' + bucket;
                html += '<li id="' + liId + '">' + '<a href="javascript:void(0);" onclick=getObjects("' + data[i]["name"] + '")>';
                html += data[i]["name"] + '</a></li>';
            }
            console.log(html);
            document.getElementById("bucketList").innerHTML = html;
            getObjects(activeBucket);
        }
    });
}

function createBucket() {
    var urlStr = "/cos/bucket/" + $("#newBucketName").val();
    console.log(urlStr);
    $.ajax({
        cache: false,
        type: "POST",
        url: urlStr,
        async: false,
        error: function (request) {
            alert("Create bucket error:" + request);
        },
        success: function (data) {
            console.log(data);
            getBuckets();
        }
    });
}

function deleteBucketAction() {
    console.log("delete bucket:" + activeBucket);
    //var nodes = document.getElementById("bucketList").childNodes;
    //for (var i = 0; i < nodes.length; ++i) {
    //    var value = nodes[i].getAttribute("class");
    //    if (value == "active") {
    //        deleteBucket(nodes[i].id.substr(2));
    //    }
    //}
    deleteBucket(activeBucket);
}

function deleteBucket(bucket) {
    var urlStr = "/cos/bucket/" + bucket;
    $.ajax({
        cache: false,
        type: "DELETE",
        url: urlStr,
        async: false,
        error: function (request) {
            alert("Delete bucket error:" + request);
        },
        success: function (data) {
            console.log(data);
            getBuckets();
        }
    });
}

function setActiveBucketListItem(bucket) {
    activeBucket = bucket;
    var liId = 'li' + bucket;
    var nodes = document.getElementById("bucketList").childNodes;
    for (var i = 0; i < nodes.length; ++i) {
        if (nodes[i].getAttribute("class")) {
            nodes[i].setAttribute("class", null);
        }
    }

    document.getElementById(liId).setAttribute("class", "active");
}

function getObjects(bucketName) {
    console.log("get " + bucketName + "'s objects");
    setActiveBucketListItem(bucketName);
    var urlStr = "/cos/bucket/" + bucketName + "/objects";
    $.ajax({
        cache: false,
        type: "GET",
        url: urlStr,
        async: false,
        error: function(request) {
            alert("Get bucket error:" + request);
        },
        success: function(data) {
            var b = document.getElementById('objectBody');
            while (b.hasChildNodes()) {
                b.removeChild(b.firstChild);
            }

            for (var i in data) {
                var element = data[i];
                var r = document.createElement('tr');

                var c = createFileTd2(element["key"]);
                r.appendChild(c);

                c = createTd(element["size"]);
                r.appendChild(c);

                var fileType = /\.[^\.]+$/.exec(element["key"]);
                c = createTd(fileType);
                r.appendChild(c);

                c = createTd(new Date(element["lastModified"]).Format("yyyy-MM-dd hh:mm:ss"));
                r.appendChild(c);

                c = createTd(element["storageClass"]);
                r.appendChild(c);

                b.appendChild(r);
            }

            //$("#objectBody :checkbox").change(function() {
            //    if (this.checked) {
            //        var url = "http://" + $("#host")[0].value + "/" + bucket + "/" + this.value;
            //        $("#objectUrl").val(url);
            //    }
            //});
            $("#objectBody :radio").change(function() {
                //console.log(this);
                if (this.checked) {
                    var url = "http://" + $("#host")[0].value + "/" + bucketName + "/" + this.value;
                    $("#objectUrl").val(url);
                    getObjectAcl(bucketName, this.value);
                }
            });
        }
    });
}

function createFileTd(bucketName, objectName) {
    var url = "/cos/bucket/" + bucketName + "/object/" + objectName + "/";    // 必须加“/”,保证后缀不被截掉
    var newLink = document.createElement('a');
    newLink.setAttribute('href', url);
    var mm = document.createTextNode(objectName);
    newLink.appendChild(mm);
   //console.log(newLink);

    var d = document.createElement('td');
    d.appendChild(newLink);

    var c = document.createElement('td');
    c.appendChild(newLink);
    return c;
}

function createFileTd2(objectName) {
    var selectHtml = '<div class="radio" style="margin: 0">\n' +
        '<label>\n' +
        '<input type="radio" name="objectRadios" value="'+ objectName + '">' + objectName  +
        '</label>\n' +
        '</div>';

    var c = document.createElement('td');
    c.innerHTML = selectHtml;
    return c;
}

function createTd(data) {
    var c = document.createElement('td');
    var m = document.createTextNode(data);
    c.appendChild(m);
    return c;
}

function deleteObjectAction() {
    var nodes = $("input[type=radio]:checked");
    var succ = false;
    for (var i = 0; i < nodes.length; ++i) {
        console.log(nodes[i].value);
        var urlStr = "/cos/bucket/" + activeBucket + "/object/" + nodes[i].value + "/";
        $.ajax({
            cache: false,
            type: "DELETE",
            url: urlStr,
            async: false,
            error: function (request) {
                alert("Delete object error:" + request);
            },
            success: function (data) {
                console.log(data);
                succ = true;
            }
        });
    }
    if (succ) {
        getObjects(activeBucket);
    }
}

function downloadObjectAction() {
    var nodes = $("input[type=radio]:checked");
    if (nodes.length > 0) {
        var urlStr = "/cos/bucket/" + activeBucket + "/object/" + nodes[0].value + "/";
        //window.location = urlStr;
        window.open(urlStr, '_blank');
        console.log(urlStr);
    }
}

function getObjectAcl(bucketName, objectName) {
    var urlStr = "/cos/bucket/" + bucketName + "/object/" + objectName + "/acl";
    $.ajax({
        cache: false,
        type: "GET",
        url: urlStr,
        async: false,
        error: function (request) {
            alert("get object acl error:" + request);
        },
        success: function (data) {
            console.log(data);
        }
    });
}

$(function () {
    $("[data-toggle='tooltip']").tooltip();

    $('#myTab a:first').tab('show');
    $('#myTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });
});

$("#fileUpload").fileinput({
    overwriteInitial: false,
    uploadUrl: "/cos/bucket/newObject",
    fileType: "any",
    maxFileSize: 10000,
    maxFilesNum: 2,
    slugCallback: function(filename) {
        console.log("slugCallback, filename:" + filename);
        return filename.replace('(', '_').replace(']', '_');
    },
    uploadExtraData: function() {
        var out = {};
        out["bucket"] = activeBucket;
        console.log(out);
        return out;
    }
}).on('fileuploaded', function(event, data, previewId, index) {
    console.log("fileuploaded");
    console.log(data);
    getObjects(data.response["bucket"]);
});
