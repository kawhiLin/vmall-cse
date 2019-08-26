package main

import (
	"flag"
	"fmt"
	"io"
	"log"
	"net/http"
	"time"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

//从环境变量中获取mysql的连接信息、metric端口
var listen_port string = "8099" //os.Getenv("METRIC_PORT")
var request_count float64
var curr_count float64
var oldCount int64
var qps float64

//设置metric端口及路径
var (
	addr = flag.String("listen-address", ":"+listen_port, "The address to listen on for HTTP requests.")
	metricsPath = flag.String("metric-path", "/metrics", "Path under which to expose metrics.")
)


//定义一个Gauge类型metric
var (
	DemoGauge = prometheus.NewGauge(prometheus.GaugeOpts{
		Name: "vmall_order_tps",
		Help: "vmall_order_tps help.",
	})
)



//注册metric
func init() {
	prometheus.MustRegister(DemoGauge)
 	//prometheus.MustRegister(DemoGauge2)
}

func getQps() {
	qps = (request_count - curr_count)/60
    fmt.Println("getQPS qps:",qps)
	fmt.Println("getQPS tps:",qps*0.8)
	fmt.Println("getQPS request_count:",request_count)
	fmt.Println("getQPS curr_count:",curr_count)
}

func handlerHello(w http.ResponseWriter, r *http.Request) {
    io.WriteString(w, "hello world")
    request_count = request_count + 1	//TODO 溢出处理
}

func main() {
	flag.Parse()
	request_count = 0
	curr_count = 0
	go func() {
		for {
			getQps() 
			DemoGauge.Set(qps)
			//DemoGauge2.Set(qps*0.8)
            //每隔60秒获取一次
			curr_count = request_count
			time.Sleep(60*time.Second)
			
		}
	}()

	// Expose the registered metrics via HTTP.
	http.Handle(*metricsPath, promhttp.Handler())
	http.HandleFunc("/hello", handlerHello)
	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte(`<html>
			<head><title>Demo Exporter</title></head>
			<body>
			<h1>Demo Exporter</h1>
			<p><a href="` + *metricsPath + `">Metrics</a></p>
			<p><a href="/hello">Hello World</a></p>
			</body>
			</html>`))
	})
	fmt.Println(*metricsPath, " serve on", *addr)
	log.Fatal(http.ListenAndServe(*addr, nil))
	fmt.Println("over")
}