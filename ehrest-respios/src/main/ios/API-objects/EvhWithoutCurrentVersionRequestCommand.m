//
// EvhWithoutCurrentVersionRequestCommand.m
//
#import "EvhWithoutCurrentVersionRequestCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWithoutCurrentVersionRequestCommand
//

@implementation EvhWithoutCurrentVersionRequestCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhWithoutCurrentVersionRequestCommand* obj = [EvhWithoutCurrentVersionRequestCommand new];
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
    if(self.realm)
        [jsonObject setObject: self.realm forKey: @"realm"];
    if(self.locale)
        [jsonObject setObject: self.locale forKey: @"locale"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.realm = [jsonObject objectForKey: @"realm"];
        if(self.realm && [self.realm isEqual:[NSNull null]])
            self.realm = nil;

        self.locale = [jsonObject objectForKey: @"locale"];
        if(self.locale && [self.locale isEqual:[NSNull null]])
            self.locale = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
