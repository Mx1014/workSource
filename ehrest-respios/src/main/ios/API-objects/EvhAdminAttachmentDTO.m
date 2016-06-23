//
// EvhAdminAttachmentDTO.m
//
#import "EvhAdminAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAttachmentDTO
//

@implementation EvhAdminAttachmentDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAdminAttachmentDTO* obj = [EvhAdminAttachmentDTO new];
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
    if(self.attachmentType)
        [jsonObject setObject: self.attachmentType forKey: @"attachmentType"];
    if(self.mustOptions)
        [jsonObject setObject: self.mustOptions forKey: @"mustOptions"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.attachmentType = [jsonObject objectForKey: @"attachmentType"];
        if(self.attachmentType && [self.attachmentType isEqual:[NSNull null]])
            self.attachmentType = nil;

        self.mustOptions = [jsonObject objectForKey: @"mustOptions"];
        if(self.mustOptions && [self.mustOptions isEqual:[NSNull null]])
            self.mustOptions = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
