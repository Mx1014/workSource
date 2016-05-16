//
// EvhImageBody.m
//
#import "EvhImageBody.h"

///////////////////////////////////////////////////////////////////////////////
// EvhImageBody
//

@implementation EvhImageBody

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhImageBody* obj = [EvhImageBody new];
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
    if(self.url)
        [jsonObject setObject: self.url forKey: @"url"];
    if(self.uri)
        [jsonObject setObject: self.uri forKey: @"uri"];
    if(self.thumbnail)
        [jsonObject setObject: self.thumbnail forKey: @"thumbnail"];
    if(self.filename)
        [jsonObject setObject: self.filename forKey: @"filename"];
    if(self.format)
        [jsonObject setObject: self.format forKey: @"format"];
    if(self.fileSize)
        [jsonObject setObject: self.fileSize forKey: @"fileSize"];
    if(self.width)
        [jsonObject setObject: self.width forKey: @"width"];
    if(self.height)
        [jsonObject setObject: self.height forKey: @"height"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.url = [jsonObject objectForKey: @"url"];
        if(self.url && [self.url isEqual:[NSNull null]])
            self.url = nil;

        self.uri = [jsonObject objectForKey: @"uri"];
        if(self.uri && [self.uri isEqual:[NSNull null]])
            self.uri = nil;

        self.thumbnail = [jsonObject objectForKey: @"thumbnail"];
        if(self.thumbnail && [self.thumbnail isEqual:[NSNull null]])
            self.thumbnail = nil;

        self.filename = [jsonObject objectForKey: @"filename"];
        if(self.filename && [self.filename isEqual:[NSNull null]])
            self.filename = nil;

        self.format = [jsonObject objectForKey: @"format"];
        if(self.format && [self.format isEqual:[NSNull null]])
            self.format = nil;

        self.fileSize = [jsonObject objectForKey: @"fileSize"];
        if(self.fileSize && [self.fileSize isEqual:[NSNull null]])
            self.fileSize = nil;

        self.width = [jsonObject objectForKey: @"width"];
        if(self.width && [self.width isEqual:[NSNull null]])
            self.width = nil;

        self.height = [jsonObject objectForKey: @"height"];
        if(self.height && [self.height isEqual:[NSNull null]])
            self.height = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
