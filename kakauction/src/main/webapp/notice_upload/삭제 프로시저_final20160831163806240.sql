select * from user_procedures;

select * from user_source
where name = upper('notice_delete');

drop procedure notice_delete;

create procedure notice_delete--���ν��� �̸� 
(
--�Ű�����
    p_no number, 
    p_groupNo number,
    p_step number
)
is
--���������
    cnt number;
begin
--ó���� ����
    --�亯�� �ִ� �������� ��� - delflag�� Y�� update
    --������ ���(�亯�� ���� ������, �亯��) - delete
    --�������� ���
    if p_step=0 then
        --�亯�� �ִ� ���
        select count(*) into cnt from notice
        where notice_groupno=p_groupno;      
        if cnt > 1 then
            update notice 
            set notice_delflag='Y'
            where notice_no=p_no;
        else
        --�亯�� ���� ���
            delete from notice
            where notice_no=p_no;
        end if;
    else
    --�亯���� ���
        delete from notice
        where notice_no=p_no;    
        --�亯���� ������ ���, ������ �����۸�
        --���´ٸ� �� �����۵� �����Ѵ�
        select count(*) into cnt from notice
        where notice_groupno=p_groupno;
        if cnt=1 then
            delete from notice a where exists (
                select 1 from notice b 
                where b.notice_no=a.notice_no and notice_groupno=p_groupno and NOTICE_DELFLAG='Y'); 
        end if;
    end if;
    commit;
EXCEPTION
    WHEN OTHERS THEN
   raise_application_error(-20001, '���� �Խ��� �ۻ��� ����!');
        ROLLBACK;
end;