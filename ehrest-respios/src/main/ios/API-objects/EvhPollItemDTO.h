//
// EvhPollItemDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
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

