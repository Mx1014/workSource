//
// EvhUserScoreDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserScoreDTO
//
@interface EvhUserScoreDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* ownerUid;

@property(nonatomic, copy) NSString* scoreType;

@property(nonatomic, copy) NSNumber* score;

@property(nonatomic, copy) NSNumber* operatorUid;

@property(nonatomic, copy) NSNumber* operateTime;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

