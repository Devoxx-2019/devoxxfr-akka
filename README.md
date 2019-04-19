# Devoxxfr Reactive Jammed Architecture Demo

* This demo was used at [DevoxxFr](https://www.devoxx.fr/) for [Reactive "Jammed" Architecture](https://cfp.devoxx.fr/2019/talk/QXB-6950/Reactive_%22Jammed%22_Architecture_ou_comment_survivre_a_l'A6_en_heure_de_pointe_!%3F)
* The slides are available in the root folder

## Docker

* Run docker-compose: `docker-compose up -d`
* Access castlemock at: `http://localhost:8888/castlemock`

# Graphite/Grafana

* Graphite: http://localhost
* Grafana: http://localhost:3000

# Loading Mock & Grafana

* Castlemock: import the mock with [monitoring/project-rest-ZQtyo0.xml](https://github.com/nsphung/devoxxfr-akka/blob/master/monitoring/project-rest-ZQtyo0.xml "Castlemock mock file")
* Grafana: import dashboard with [monitoring/Reactive%20Jammed%20Architecture-1555355905499.json](https://github.com/nsphung/devoxxfr-akka/blob/master/monitoring/Reactive%20Jammed%20Architecture-1555355905499.json "Grafana Dashboard file")

# How to start

* Run the main.scala file
* Enjoy the backpressure on Grafana Reactive Jammed Architecture !

Source: 
* https://graphite.readthedocs.io/en/latest/install.html 
* https://www.linode.com/docs/uptime/monitoring/install-graphite-and-grafana/#docker-compose-configuration
