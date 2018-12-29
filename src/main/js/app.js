const express = require('express');
const bodyParser = require('body-parser');
const convertFromRaw = require('draft-js').convertFromRaw;

const app = express();

app.use(bodyParser.json({limit: '500mb'}));

app.post('/blog-post/validation', (request, response) => {
    const blogPostContent = request.body;

    try {
        const parsedBlogPostContent = convertFromRaw(blogPostContent);

        if (parsedBlogPostContent.getPlainText().length > 10000) {
            response.status(400);
            response.send({message: 'CONTENT_IS_TOO_LONG'});
        } else {
            response.status(200);
            response.send({
                message: 'NOT_AVAILABLE',
                plainText: parsedBlogPostContent.getPlainText()
            });
        }
    } catch (e) {
        response.status(400);
        response.send({message: 'INVALID_CONTENT'})
    }
});

app.listen(3001);
console.log('started app');