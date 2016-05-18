//
// EvhSearchApplyCardCommand.m
//
#import "EvhSearchApplyCardCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchApplyCardCommand
//

@implementation EvhSearchApplyCardCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSearchApplyCardCommand* obj = [EvhSearchApplyCardCommand new];
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
    if(self.applyStatus)
        [jsonObject setObject: self.applyStatus forKey: @"applyStatus"];
    if(self.applierName)
        [jsonObject setObject: self.applierName forKey: @"applierName"];
    if(self.applierPhone)
        [jsonObject setObject: self.applierPhone forKey: @"applierPhone"];
    if(self.plateNumber)
        [jsonObject setObject: self.plateNumber forKey: @"plateNumber"];
    if(self.beginDay)
        [jsonObject setObject: self.beginDay forKey: @"beginDay"];
    if(self.endDay)
        [jsonObject setObject: self.endDay forKey: @"endDay"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.applyStatus = [jsonObject objectForKey: @"applyStatus"];
        if(self.applyStatus && [self.applyStatus isEqual:[NSNull null]])
            self.applyStatus = nil;

        self.applierName = [jsonObject objectForKey: @"applierName"];
        if(self.applierName && [self.applierName isEqual:[NSNull null]])
            self.applierName = nil;

        self.applierPhone = [jsonObject objectForKey: @"applierPhone"];
        if(self.applierPhone && [self.applierPhone isEqual:[NSNull null]])
            self.applierPhone = nil;

        self.plateNumber = [jsonObject objectForKey: @"plateNumber"];
        if(self.plateNumber && [self.plateNumber isEqual:[NSNull null]])
            self.plateNumber = nil;

        self.beginDay = [jsonObject objectForKey: @"beginDay"];
        if(self.beginDay && [self.beginDay isEqual:[NSNull null]])
            self.beginDay = nil;

        self.endDay = [jsonObject objectForKey: @"endDay"];
        if(self.endDay && [self.endDay isEqual:[NSNull null]])
            self.endDay = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

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
