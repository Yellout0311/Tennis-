// 필요한 모듈을 불러옵니다.
const express = require('express');
const fs = require('fs');
const path = require('path');

// express 애플리케이션을 생성합니다.
const app = express();

// 포트 번호를 설정합니다.
const PORT = 3000;

// JSON 파일을 반환하는 라우트를 만듭니다.
app.get('/data', (req, res) => {
    const filePath = path.join(__dirname, 'data.json');

    // JSON 파일을 읽어와서 클라이언트에 응답합니다.
    fs.readFile(filePath, 'utf8', (err, data) => {
        if (err) {
            console.error('파일 읽기 오류:', err);
            res.status(500).json({ error: '파일을 읽을 수 없습니다.' });
        } else {
            res.json(JSON.parse(data));
        }
    });
});

// 서버를 시작합니다.
app.listen(PORT, () => {
    console.log(`서버가 포트 ${PORT}에서 실행 중입니다.`);
});
