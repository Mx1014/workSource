//
// EvhListPunchExceptionRequestCommand.m
//
#import "EvhListPunchExceptionRequestCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPunchExceptionRequestCommand
//

@implementation EvhListPunchExceptionRequestCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPunchExceptionRequestCommand* obj = [EvhListPunchExceptionRequestCommand new];
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
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.keyword)
        [jsonObject setObject: self.keyword forKey: @"keyword"];
    if(self.startDay)
        [jsonObject setObject: self.startDay forKey: @"startDay"];
    if(self.endDay)
        [jsonObject setObject: self.endDay forKey: @"endDay"];
    if(self.exceptionStatus)
        [jsonObject setObject: self.exceptionStatus forKey: @"exceptionStatus"];
    if(self.processCode)
        [jsonObject setObject: self.processCode forKey: @"processCode"];
    if(self.pageOffset)
        [jsonObject setObject: self.pageOffset forKey: @"pageOffset"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.keyword = [jsonObject objectForKey: @"keyword"];
        if(self.keyword && [self.keyword isEqual:[NSNull null]])
            self.keyword = nil;

        self.startDay = [jsonObject objectForKey: @"startDay"];
        if(self.startDay && [self.startDay isEqual:[NSNull null]])
            self.startDay = nil;

        self.endDay = [jsonObject objectForKey: @"endDay"];
        if(self.endDay && [self.endDay isEqual:[NSNull null]])
            self.endDay = nil;

        self.exceptionStatus = [jsonObject objectForKey: @"exceptionStatus"];
        if(self.exceptionStatus && [self.exceptionStatus isEqual:[NSNull null]])
            self.exceptionStatus = nil;

        self.processCode = [jsonObject objectForKey: @"processCode"];
        if(self.processCode && [self.processCode isEqual:[NSNull null]])
            self.processCode = nil;

        self.pageOffset = [jsonObject objectForKey: @"pageOffset"];
        if(self.pageOffset && [self.pageOffset isEqual:[NSNull null]])
            self.pageOffset = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
