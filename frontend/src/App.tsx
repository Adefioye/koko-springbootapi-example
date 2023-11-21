import Navbar from "./components/shared/Navbar";
import LeftSideBar from "./components/shared/LeftSideBar";
import ImageGallery from "./components/ImageGallery";

function App() {
  return (
    <div className="relative flex flex-col min-h-screen items-center text-white bg-slate-600">
      {/* Navbar */}
      <Navbar />
      {/* Left side bar */}
      <LeftSideBar />
      {/* Image gallery */}
      <ImageGallery />
    </div>
  );
}

export default App;
