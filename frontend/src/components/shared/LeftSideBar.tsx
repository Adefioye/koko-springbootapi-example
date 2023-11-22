const LeftSideBar = () => {
  return (
    <div className="absolute top-0 left-0 w-[15rem] z-50 min-h-screen flex flex-col space-y-5 shadow-xl overflow-y-auto">
      <h3 className="text-center font-serif font-bold text-lg mt-5">
        Dashboard
      </h3>
      {/* Image */}
      <div className="w-20 h-20 border-2 mx-auto flex justify-center items-center rounded-full">
        <h1 className="font-mono text-lg">KOKO</h1>
      </div>
      <div className="flex flex-col space-y-3 ml-4">
        <p className="cursor-pointer">Home</p>
        <p className="cursor-pointer">Trending</p>
        <p className="cursor-pointer">Explore</p>
        <p className="cursor-pointer">Favorites</p>
        <p className="cursor-pointer">Settings</p>
      </div>
    </div>
  );
};

export default LeftSideBar;
