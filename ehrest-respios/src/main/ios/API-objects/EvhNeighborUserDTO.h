//
// EvhNeighborUserDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNeighborUserDTO
//
@interface EvhNeighborUserDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* userAvatarUri;

@property(nonatomic, copy) NSString* userAvatarUrl;

@property(nonatomic, copy) NSString* userStatusLine;

@property(nonatomic, copy) NSNumber* neighborhoodRelation;

@property(nonatomic, copy) NSNumber* distance;

@property(nonatomic, copy) NSString* occupation;

@property(nonatomic, copy) NSString* initial;

@property(nonatomic, copy) NSString* fullPinyin;

@property(nonatomic, copy) NSString* fullInitial;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

