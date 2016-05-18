//
// EvhQueryDoorAccessAdminCommand.m
//
#import "EvhQueryDoorAccessAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryDoorAccessAdminCommand
//

@implementation EvhQueryDoorAccessAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQueryDoorAccessAdminCommand* obj = [EvhQueryDoorAccessAdminCommand new];
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
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.search)
        [jsonObject setObject: self.search forKey: @"search"];
    if(self.linkStatus)
        [jsonObject setObject: self.linkStatus forKey: @"linkStatus"];
    if(self.doorType)
        [jsonObject setObject: self.doorType forKey: @"doorType"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.search = [jsonObject objectForKey: @"search"];
        if(self.search && [self.search isEqual:[NSNull null]])
            self.search = nil;

        self.linkStatus = [jsonObject objectForKey: @"linkStatus"];
        if(self.linkStatus && [self.linkStatus isEqual:[NSNull null]])
            self.linkStatus = nil;

        self.doorType = [jsonObject objectForKey: @"doorType"];
        if(self.doorType && [self.doorType isEqual:[NSNull null]])
            self.doorType = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
