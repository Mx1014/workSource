//
// EvhListNewsCommentResponse.m
//
#import "EvhListNewsCommentResponse.h"
#import "EvhNewsCommentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNewsCommentResponse
//

@implementation EvhListNewsCommentResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListNewsCommentResponse* obj = [EvhListNewsCommentResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _commentList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.commentList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhNewsCommentDTO* item in self.commentList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"commentList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"commentList"];
            for(id itemJson in jsonArray) {
                EvhNewsCommentDTO* item = [EvhNewsCommentDTO new];
                
                [item fromJson: itemJson];
                [self.commentList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
