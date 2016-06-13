//
// EvhSearchRechargeRecordCommand.m
//
#import "EvhSearchRechargeRecordCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchRechargeRecordCommand
//

@implementation EvhSearchRechargeRecordCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSearchRechargeRecordCommand* obj = [EvhSearchRechargeRecordCommand new];
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
    if(self.ownerName)
        [jsonObject setObject: self.ownerName forKey: @"ownerName"];
    if(self.rechargePhone)
        [jsonObject setObject: self.rechargePhone forKey: @"rechargePhone"];
    if(self.plateNumber)
        [jsonObject setObject: self.plateNumber forKey: @"plateNumber"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.rechargeStatus)
        [jsonObject setObject: self.rechargeStatus forKey: @"rechargeStatus"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerName = [jsonObject objectForKey: @"ownerName"];
        if(self.ownerName && [self.ownerName isEqual:[NSNull null]])
            self.ownerName = nil;

        self.rechargePhone = [jsonObject objectForKey: @"rechargePhone"];
        if(self.rechargePhone && [self.rechargePhone isEqual:[NSNull null]])
            self.rechargePhone = nil;

        self.plateNumber = [jsonObject objectForKey: @"plateNumber"];
        if(self.plateNumber && [self.plateNumber isEqual:[NSNull null]])
            self.plateNumber = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.rechargeStatus = [jsonObject objectForKey: @"rechargeStatus"];
        if(self.rechargeStatus && [self.rechargeStatus isEqual:[NSNull null]])
            self.rechargeStatus = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
