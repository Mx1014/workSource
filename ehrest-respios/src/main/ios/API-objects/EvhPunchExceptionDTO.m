//
// EvhPunchExceptionDTO.m
//
#import "EvhPunchExceptionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchExceptionDTO
//

@implementation EvhPunchExceptionDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPunchExceptionDTO* obj = [EvhPunchExceptionDTO new];
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
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.exceptionComment)
        [jsonObject setObject: self.exceptionComment forKey: @"exceptionComment"];
    if(self.requestType)
        [jsonObject setObject: self.requestType forKey: @"requestType"];
    if(self.processCode)
        [jsonObject setObject: self.processCode forKey: @"processCode"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.exceptionComment = [jsonObject objectForKey: @"exceptionComment"];
        if(self.exceptionComment && [self.exceptionComment isEqual:[NSNull null]])
            self.exceptionComment = nil;

        self.requestType = [jsonObject objectForKey: @"requestType"];
        if(self.requestType && [self.requestType isEqual:[NSNull null]])
            self.requestType = nil;

        self.processCode = [jsonObject objectForKey: @"processCode"];
        if(self.processCode && [self.processCode isEqual:[NSNull null]])
            self.processCode = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
