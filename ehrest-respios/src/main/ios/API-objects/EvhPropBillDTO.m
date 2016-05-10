//
// EvhPropBillDTO.m
//
#import "EvhPropBillDTO.h"
#import "EvhPropBillItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropBillDTO
//

@implementation EvhPropBillDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPropBillDTO* obj = [EvhPropBillDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _itemList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.entityType)
        [jsonObject setObject: self.entityType forKey: @"entityType"];
    if(self.entityId)
        [jsonObject setObject: self.entityId forKey: @"entityId"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.dateStr)
        [jsonObject setObject: self.dateStr forKey: @"dateStr"];
    if(self.totalAmount)
        [jsonObject setObject: self.totalAmount forKey: @"totalAmount"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.notifyCount)
        [jsonObject setObject: self.notifyCount forKey: @"notifyCount"];
    if(self.notifyTime)
        [jsonObject setObject: self.notifyTime forKey: @"notifyTime"];
    if(self.itemList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPropBillItemDTO* item in self.itemList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"itemList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.entityType = [jsonObject objectForKey: @"entityType"];
        if(self.entityType && [self.entityType isEqual:[NSNull null]])
            self.entityType = nil;

        self.entityId = [jsonObject objectForKey: @"entityId"];
        if(self.entityId && [self.entityId isEqual:[NSNull null]])
            self.entityId = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.dateStr = [jsonObject objectForKey: @"dateStr"];
        if(self.dateStr && [self.dateStr isEqual:[NSNull null]])
            self.dateStr = nil;

        self.totalAmount = [jsonObject objectForKey: @"totalAmount"];
        if(self.totalAmount && [self.totalAmount isEqual:[NSNull null]])
            self.totalAmount = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.notifyCount = [jsonObject objectForKey: @"notifyCount"];
        if(self.notifyCount && [self.notifyCount isEqual:[NSNull null]])
            self.notifyCount = nil;

        self.notifyTime = [jsonObject objectForKey: @"notifyTime"];
        if(self.notifyTime && [self.notifyTime isEqual:[NSNull null]])
            self.notifyTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"itemList"];
            for(id itemJson in jsonArray) {
                EvhPropBillItemDTO* item = [EvhPropBillItemDTO new];
                
                [item fromJson: itemJson];
                [self.itemList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
