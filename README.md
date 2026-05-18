# **서울 테니스 코트 앱**  
서울시 공공 테니스 코트 정보를 조회하고, 즐겨찾기 및 예약 일정을 관리할 수 있는 Android 앱입니다.
### 주요 기능
1. 코트 목록

서울 공공 테니스 코트 리스트 조회
즐겨찾기 추가 / 제거

2. 지도

Google Maps API를 활용한 코트 위치 마커 표시
마커 클릭 시 코트명, 전화번호, 주소, 이미지 확인
전화번호 클립보드 복사
Google Maps 길찾기 연동

3. 즐겨찾기

즐겨찾기로 등록한 코트만 모아보기

4. 캘린더

날짜별 예약/예매 일정 등록 및 관리
파란 점: 예약일 / 빨간 점: 예매일 구분 표시

### 기술 스택

언어: Kotlin, Java
아키텍처: MVVM (ViewModel + LiveData)
UI: Fragment, RecyclerView, BottomNavigationView
지도: Google Maps SDK for Android
캘린더: MaterialCalendarView
이미지 로딩: Glide
네트워크: OkHttp
데이터 저장: SharedPreferences
공공 데이터: 서울 열린데이터광장 - 공공서비스예약 체육시설 API

### 📂 프로젝트 구조
com.example.tennis  
├── MainActivity.kt  
├── data  
│   ├── RemoteDataClass.java       # 서울 공공 API 통신  
│   └── SharedPreferencesHelper.kt # 즐겨찾기 로컬 저장  
└── ui  
    ├── courts  
    │   ├── CourtFragment.kt  
    │   ├── MyCourtRecyclerViewAdapter.kt  
    │   └── placeholder  
    │       └── PlaceholderContent.kt  
    ├── map  
    │   └── MapViewFragment.kt  
    ├── starred  
    │   ├── StarredCourtFragment.kt  
    │   └── MyStarredCourtRecyclerViewAdapter.kt  
    └── calendar  
        ├── CalendarFragment.kt  
        ├── CalendarViewModel.kt  
        └── EventDecorator.kt  
        
### API 키 설정
이 앱은 Google Maps API Key가 필요합니다.
AndroidManifest.xml에 아래와 같이 추가하세요.
xml<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_API_KEY_HERE" />



### 참고사항

현재 코트 목록은 더미 데이터(PlaceholderContent)로 구성되어 있습니다.
캘린더 예약 정보는 앱 재시작 시 초기화됩니다 (로컬 저장 미구현).
미배포 프로젝트입니다.
