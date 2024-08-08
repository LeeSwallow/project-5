


document.addEventListener('DOMContentLoaded', () => {
    const introSection = document.getElementById('intro');
    const features = document.querySelectorAll('.feature');
    const finalSection = document.getElementById('final');
    const toggleButton = document.getElementById('toggle-button');

    // 페이지가 로드된 후 intro 애니메이션을 트리거합니다
    setTimeout(() => {
        introSection.classList.add('visible');
    }, 100); // 페이지 로드 후 잠시 대기 후 애니메이션 시작

    // 화면 크기에 따라 이미지 변경
    const updateImages = () => {
        const isMobile = window.innerWidth <= 600;
        document.querySelectorAll('.feature-image img').forEach((img) => {
            const newSrc = isMobile ? img.dataset.mobile : img.dataset.desktop;
            img.src = newSrc;
        });
    };

    // 스크롤 이벤트를 통해 feature 애니메이션을 트리거합니다
    const handleScroll = () => {
        features.forEach((feature) => {
            const rect = feature.getBoundingClientRect();
            if (rect.top < window.innerHeight) {
                feature.querySelector('.feature-content').classList.add('visible');
            }
        });

        // 마지막 섹션 애니메이션 트리거
        const finalRect = finalSection.getBoundingClientRect();
        if (finalRect.top < window.innerHeight) {
            finalSection.classList.add('visible');
        }
    };

    // 버튼 상태 변경
    toggleButton.addEventListener('click', () => {
        toggleButton.classList.toggle('on');
    });

    window.addEventListener('scroll', handleScroll);
    window.addEventListener('resize', updateImages);

    handleScroll(); // 초기 스크롤 상태에서 확인
    updateImages(); // 초기 화면 크기에 따라 이미지 설정
});


function redirect(){
    location.href = "http://localhost:8080/subject/main";

}