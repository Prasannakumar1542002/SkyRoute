import { useEffect, useState } from 'react'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import './App.css'
import SkyMap from './components/SkyMap'
import SplashScreen from './components/SplashScreen'
import DemoProvider from './components/DemoProvider';

function App() {
  const [isDesktop, setIsDesktop] = useState(true);

  useEffect(() => {
    const handleResize = () => {
      setIsDesktop(window.innerWidth >= 1560); // 1024px = typical desktop breakpoint
    };

    handleResize(); // check on load
    window.addEventListener("resize", handleResize);

    return () => window.removeEventListener("resize", handleResize);
  }, []);

  if (!isDesktop) {
    return (
      <div className="flex flex-col items-center justify-center h-screen bg-gradient-to-br from-blue-50 to-indigo-100 text-center px-6">
        <h1 className="text-2xl font-bold text-gray-800 mb-4">
          SkyRoute is currently available on Desktop Only 💻
        </h1>

        <p className="text-lg text-gray-700 max-w-md mb-6 leading-relaxed">
          Dive into <strong>intelligent flight paths</strong>,
          explore <strong>global connections</strong>, and
          interact with <strong>dynamic maps</strong> in real time.
        </p>

        <p className="text-base text-indigo-600 font-semibold mb-2">
          Bigger screens unlock bigger adventures. See you on desktop 😉
        </p>

        <p className="text-sm text-gray-500">
          Don’t worry — we’re working on a <strong>responsive mobile layout </strong>
          so you’ll soon enjoy SkyRoute on any device.
        </p>
      </div>
    );
  }

  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<SplashScreen />} />
          <Route path="/path" element={<DemoProvider />} />
        </Routes>
      </Router>
    </>
  )
}

export default App
