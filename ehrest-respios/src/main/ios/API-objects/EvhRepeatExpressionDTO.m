//
// EvhRepeatExpressionDTO.m
//
#import "EvhRepeatExpressionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRepeatExpressionDTO
//

@implementation EvhRepeatExpressionDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRepeatExpressionDTO* obj = [EvhRepeatExpressionDTO new];
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
    if(self.year)
        [jsonObject setObject: self.year forKey: @"year"];
    if(self.month)
        [jsonObject setObject: self.month forKey: @"month"];
    if(self.day)
        [jsonObject setObject: self.day forKey: @"day"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.year = [jsonObject objectForKey: @"year"];
        if(self.year && [self.year isEqual:[NSNull null]])
            self.year = nil;

        self.month = [jsonObject objectForKey: @"month"];
        if(self.month && [self.month isEqual:[NSNull null]])
            self.month = nil;

        self.day = [jsonObject objectForKey: @"day"];
        if(self.day && [self.day isEqual:[NSNull null]])
            self.day = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
