//
// EvhPollItemDTO.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollItemDTO
//
@interface EvhPollItemDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* itemId;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* coverUrl;

@property(nonatomic, copy) NSNumber* voteCount;

@property(nonatomic, copy) NSString* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

