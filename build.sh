#!/bin/bash
    # 获取工作目录
    basepath=$(cd `dirname $0`;pwd); # ../vmall/
    # 镜像构建版本
    image_version=$1;
	echo $image_version;
echo "构建版本：$1";
    cd $basepath;
    mkdir -p images;
    mvn clean install;
    for dir in $(ls|grep vmall); do
        cd $basepath/$dir;
        echo "########################";
        echo “当前目录：”`pwd`;
		echo "开始构建镜像：${dir}:${image_version}";
        echo "########################";

        docker build -t "${dir}:${image_version}" .;
        docker save -o "${dir}-${image_version}.tar" "${dir}:${image_version}";
        mv "${dir}-${image_version}.tar" "${basepath}/images";
        chown -R nobody:nobody "${basepath}";
    done