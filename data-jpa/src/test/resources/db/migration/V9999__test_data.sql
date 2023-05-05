INSERT INTO public.user_ (id,bio,email,image,password_hash,username) VALUES
	 (1,NULL,'ivan@mail.com',NULL,'$2a$10$3.tqZQdqKJC1vxISkDCiUuw3ARZRk/F/.E2vF0rC8Zf/z6YDnDzXS','ivan'),
	 (2,NULL,'james@gmail.com',NULL,'$2a$10$h1d.0WcRcIj.2KIeMFGSY.tp0LcqYVwVC5uV/IdGj2o1N6Zrh4Ota','james'),
	 (3,NULL,'simpson@gmail.com',NULL,'$2a$10$X1p4cOVey6GUG1wfK/xTN.RORwQonDGejF6DopM.zSxKRYojoEVX6','simpson');

SELECT setval('user__seq', 4, true);

INSERT INTO public.users_follow (follower_id,following_id) VALUES
	 (1,2),
	 (1,3),
	 (2,1),
	 (2,3),
	 (3,1),
	 (3,2);

INSERT INTO public.tag (id,name) VALUES
	 (1,'java'),
	 (2,'ruby'),
	 (3,'performance'),
	 (4,'properties');

SELECT setval('tag_seq', 5, true);


INSERT INTO public.article (id,created_at,updated_at,body,description,slug,title,author_id) VALUES
	 (1,'2023-05-05 12:52:59.490','2023-05-05 12:52:59.490','Effective Java Body','Effective Java Description','effective-java','Effective Java',2);

SELECT setval('article_seq', 2, true);

INSERT INTO public.article_tags (article_id,tags_id) VALUES
	 (1,1),
	 (1,4);

INSERT INTO public.article_favouring_users (article_id,favouring_users_id) VALUES
	 (1,1),
	 (1,2),
	 (1,3);


