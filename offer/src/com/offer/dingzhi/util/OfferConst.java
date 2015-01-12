package com.offer.dingzhi.util;

public interface OfferConst {

	//�������ƣ�ʡcode������code
	public static  String[][] CITYS = {
		{"����","330000","330100"},
		{"����","110000","110100"},
		{"���","120000","120100"},
		{"�Ϻ�","310000","310100"},
		{"�Ͼ�","320000","320100"},
		{"����","440000","440100"},
		{"�人","420000","420100"},
		{"�ɶ�","510000","510100"},
		{"����","500000","500100"},
		{"�Ϸ�","340000","340100"},
		{"����","610000","610000"},
		{"������","230000","230100"},
		{"����","210000","210200"},
		{"��ɳ","430000","430100"}
	};
	//ȫ�ƣ���ƣ����д��룬�Ƿ��ǵط������֪��ԺУ
	public static String[][] US = {
		
		//���ݵĴ�ѧ
		{"�㽭��ѧ","���","330100","1"},
		{"���ݵ��ӿƼ���ѧ","����","330100","1"},
		{"�㽭��ҵ��ѧ","�㹤��","330100","0"},
		{"�㽭������ѧ","������","330100","0"},
		{"�㽭���̴�ѧ","�㽭����","330100","0"},
		{"�㽭ʦ����ѧ","�㽭ʦ��","330100","0"},
		{"����ʦ����ѧ","����ʦ��","330100","0"},

		//�����Ĵ�ѧ
		{"������ѧ","����","110100","1"},
		{"�廪��ѧ","�廪","110100","1"},
		{"�������캽�մ�ѧ","����","110100","0"},
		{"�����ʵ��ѧ","����","110100","0"},
		{"�й������ѧ","�˴�","110100","0"},
		{"����������ѧ","����","110100","0"},
		{"�й���ѧԺ","�п�Ժ","110100","0"},

		//���Ĵ�ѧ
		{"����ѧ","���","120100","1"},
		{"�Ͽ���ѧ","�ϴ�","120100","1"},
		{"�ӱ���ҵ��ѧ","�ӹ���","120100","0"},
		{"���������ѧ","�������","120100","0"},
		{"���Ƽ���ѧ","���Ƽ�","120100","0"},
		{"���ҵ��ѧ","���ҵ","120100","0"},
		{"�й��񺽴�ѧ","�й���","120110","0"},
		
		//�Ϻ��Ĵ�ѧ
		{"������ѧ","����","310100","1"},
		{"�Ϻ���ͨ��ѧ","�Ͻ���","310100","1"},
		{"ͬ�ô�ѧ","ͬ��","310100","0"},
		{"����������ѧ","��������","310100","0"},
		{"����ʦ����ѧ","����ʦ��","310100","0"},
		{"�Ϻ��ƾ���ѧ","�ϲ�","310100","0"},
		{"�Ϻ���ѧ","�Ϻ���ѧ","310100","0"},
		
		//�Ͼ��Ĵ�ѧ
		{"�Ͼ���ѧ","�ϴ�","320100","1"},
		{"���ϴ�ѧ","���ϴ�ѧ","320100","1"},
		{"�Ͼ����캽�մ�ѧ","�Ϻ�","320100","0"},
		{"�Ͼ�������ѧ","����","320100","0"},
		{"���ϴ�ѧ","���ϴ�ѧ","320100","0"},
		{"���ݴ�ѧ","���ݴ�ѧ","320100","0"},
		{"�Ͼ���ҵ��ѧ","�Ϲ���","320100","0"},
		
		//���ݵĴ�ѧ
		{"��ɽ��ѧ","��ɽ","440100","1"},
		{"����������ѧ","��������","440100","1"},
		{"���ϴ�ѧ","����","440100","0"},
		{"���ݴ�ѧ","����","440100","0"},
		{"����ʦ����ѧ","����ʦ��","440100","0"},
		{"����ũҵ��ѧ","����ũ��","440100","0"},
		{"��ͷ��ѧ","��ͷ��ѧ","440100","0"},
		
		//�人�Ĵ�ѧ
		{"�人��ѧ","���","420100","1"},
		{"���пƼ���ѧ","���ƴ�","420100","1"},
		{"�人������ѧ","�人����","420100","0"},
		{"����ʦ����ѧ","����ʦ��","420100","0"},
		{"�人���̴�ѧ","�人����","420100","0"},
		{"�人�Ƽ���ѧ","�人�Ƽ�","420100","0"},
		{"������ѧ","������ѧ","420100","0"},
		
		//�ɶ��Ĵ�ѧ
		{"�Ĵ���ѧ","����","510100","1"},
		{"�ɶ����ӿƼ���ѧ","���ӿƴ�","510100","1"},
		{"���Ͻ�ͨ��ѧ","���Ͻ���","510100","0"},
		{"���ϲƾ���ѧ","����","510100","0"},
		{"�ɶ�������ѧ","�ɶ�����","510100","0"},
		{"���ϿƼ���ѧ","���ϿƼ�","510100","0"},
		{"�Ĵ�ʦ����ѧ","��ʦ��","510100","0"},
		
		//����Ĵ�ѧ
		{"�����ѧ","�ش�","500100","1"},
		{"�����ʵ��ѧ","����","500100","1"},
		{"���ϴ�ѧ","���ϴ�ѧ","500100","0"},
		{"���콻ͨ��ѧ","�ؽ�","500100","0"},
		{"���칤�̴�ѧ","���칤��","500100","0"},
		{"����Ƽ���ѧ","����Ƽ�","500100","0"},
		{"����ʦ����ѧ","��ʦ��","500100","0"},
		
		//�ϷʵĴ�ѧ
		{"�й���ѧ������ѧ","","�пƴ�","1"},
		{"���մ�ѧ","���մ�ѧ","340100","0"},
		{"�Ϸʹ�ҵ��ѧ","�Ϸʹ�ҵ","340100","0"},
		{"���չ�ҵ��ѧ","���չ�ҵ","340100","0"},
		{"����������ѧ","��������","340100","0"},
		//�����Ĵ�ѧ
		{"������ͨ��ѧ","��������","610000","1"},
		{"�������ӿƼ���ѧ","�������ӿƼ�","610000","0"},
		{"������ѧ","������ѧ","610000","0"},
		{"����ʦ����ѧ","����ʦ��","610000","0"},
		{"����������ѧ","��������","610000","0"},
		{"������ҵ��ѧ","������ҵ","610000","0"},
		//�������Ĵ�ѧ
		{"��������ҵ��ѧ","������","230100","1"},
		{"���������̴�ѧ","����������","230100","0"},
		{"����ũҵ��ѧ","����ũҵ","230100","0"},
		{"������ҵ��ѧ","������ҵ","230100","0"},
		{"��������ѧ","������","230100","0"},
        //�����Ĵ�ѧ		
		{"����������ѧ","��������","210200","1"},
		{"������ѧ","������ѧ","210200","0"},
		{"������ѧ","������ѧ","210200","0"},
		{"������ҵ��ѧ","������ҵ","210200","0"},
		{"�������´�ѧ","��������","210200","0"},
		//��ɳ�Ĵ�ѧ
		{"���ϴ�ѧ","����","430100","1"},
		{"�����Ƽ���ѧ","�����ƴ�","430100","1"},
		{"���ϴ�ѧ","����","430100","0"},
		{"����ʦ����ѧ","����ʦ��","430100","0"},
		{"��̶��ѧ","��̶","430100","0"},
		{"��ɳ������ѧ","��ɳ����","430100","0"},
	};
}