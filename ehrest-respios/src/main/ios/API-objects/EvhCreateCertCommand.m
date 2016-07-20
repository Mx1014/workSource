//
// EvhCreateCertCommand.m
//
#import "EvhCreateCertCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateCertCommand
//

@implementation EvhCreateCertCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateCertCommand* obj = [EvhCreateCertCommand new];
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
    if(self.certType)
        [jsonObject setObject: self.certType forKey: @"certType"];
    if(self.certKey)
        [jsonObject setObject: self.certKey forKey: @"certKey"];
    if(self.certPass)
        [jsonObject setObject: self.certPass forKey: @"certPass"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.certType = [jsonObject objectForKey: @"certType"];
        if(self.certType && [self.certType isEqual:[NSNull null]])
            self.certType = nil;

        self.certKey = [jsonObject objectForKey: @"certKey"];
        if(self.certKey && [self.certKey isEqual:[NSNull null]])
            self.certKey = nil;

        self.certPass = [jsonObject objectForKey: @"certPass"];
        if(self.certPass && [self.certPass isEqual:[NSNull null]])
            self.certPass = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
