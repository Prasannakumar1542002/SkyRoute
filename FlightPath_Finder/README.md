# ✈️ SkyRoute

**SkyRoute** is a full-stack web application that helps users discover and visualize the shortest flight path between any two airports in the world. Powered by **Dijkstra’s Algorithm**, it provides optimal routing based on real-world airport and flight data.

## 🌐 Live Demo


## 🧠 Project Summary

SkyRoute models the world’s airports and flights as a graph, where:
- **Airports** = Nodes
- **Flights** = Edges

The backend calculates the most efficient path between two airports, considering both **distance** and **flight availability**. The frontend visualizes the result on a responsive, interactive **world map** using **Leaflet.js** or **Mapbox**.

This project demonstrates core concepts in **graph algorithms**, **data structures**, **backend API design**, and **frontend mapping tools**—making it an educational and visually engaging portfolio project.

---

## 🔧 Tech Stack

### Backend
- **Spring Boot**
- **Java**
- **Dijkstra’s Algorithm (Custom Implementation)**
- **MySQL** (or any relational DB for airport/flight data)
- **RESTful APIs**

### Frontend
- **React.js**
- **Leaflet.js** / **Mapbox** for map rendering
- **Axios** for API communication
- **Tailwind CSS** or **Bootstrap** (optional styling)

---

## 📦 Features

- ✈️ Find the shortest route between two global airports
- 🌍 Visualize routes on a world map
- 🔁 Handle multiple route recalculations in real time
- 📊 Real flight data integration (with availability)
- 🔍 Input suggestions and error handling

---

## ⚙️ How to Run Locally

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/skyroute.git
cd skyroute
