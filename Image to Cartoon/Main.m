% To read the image from the folder
img3 = double(imread('ele.jpg'))/255;


% Invoking the function to convert to cartoon
cartoon_img3 = cartoon(img3);

% Display color input image

figure(3); clf;
set(gcf,'Name','Orginal Image');
imagesc(img3); axis image;
title('Orginal Image');

figure(5); clf;
set(gcf,'Name','Cartoon Image');
A1=medfilt3(cartoon_img3);
imagesc(A1); axis image;
title('Cartoon Image');
