//
// EvhGroupContactDTO.m
//
#import "EvhGroupContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupContactDTO
//

@implementation EvhGroupContactDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGroupContactDTO* obj = [EvhGroupContactDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.contactUid)
        [jsonObject setObject: self.contactUid forKey: @"contactUid"];
    if(self.contactType)
        [jsonObject setObject: self.contactType forKey: @"contactType"];
    if(self.contactToken)
        [jsonObject setObject: self.contactToken forKey: @"contactToken"];
    if(self.contactName)
        [jsonObject setObject: self.contactName forKey: @"contactName"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.updateUid)
        [jsonObject setObject: self.updateUid forKey: @"updateUid"];
    if(self.updateTime)
        [jsonObject setObject: self.updateTime forKey: @"updateTime"];
    if(self.department)
        [jsonObject setObject: self.department forKey: @"department"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.contactUid = [jsonObject objectForKey: @"contactUid"];
        if(self.contactUid && [self.contactUid isEqual:[NSNull null]])
            self.contactUid = nil;

        self.contactType = [jsonObject objectForKey: @"contactType"];
        if(self.contactType && [self.contactType isEqual:[NSNull null]])
            self.contactType = nil;

        self.contactToken = [jsonObject objectForKey: @"contactToken"];
        if(self.contactToken && [self.contactToken isEqual:[NSNull null]])
            self.contactToken = nil;

        self.contactName = [jsonObject objectForKey: @"contactName"];
        if(self.contactName && [self.contactName isEqual:[NSNull null]])
            self.contactName = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.updateUid = [jsonObject objectForKey: @"updateUid"];
        if(self.updateUid && [self.updateUid isEqual:[NSNull null]])
            self.updateUid = nil;

        self.updateTime = [jsonObject objectForKey: @"updateTime"];
        if(self.updateTime && [self.updateTime isEqual:[NSNull null]])
            self.updateTime = nil;

        self.department = [jsonObject objectForKey: @"department"];
        if(self.department && [self.department isEqual:[NSNull null]])
            self.department = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
